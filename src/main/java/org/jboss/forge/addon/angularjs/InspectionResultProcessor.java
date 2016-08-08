/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import static org.jboss.forge.addon.angularjs.AngularJSInspectionResultConstants.JS_IDENTIFIER;
import static org.jboss.forge.addon.scaffold.metawidget.inspector.ForgeInspectionResultConstants.PRIMARY_KEY;
import static org.metawidget.inspector.InspectionResultConstants.DATETIME_TYPE;
import static org.metawidget.inspector.InspectionResultConstants.LABEL;
import static org.metawidget.inspector.InspectionResultConstants.LOOKUP;
import static org.metawidget.inspector.InspectionResultConstants.NAME;
import static org.metawidget.inspector.InspectionResultConstants.TYPE;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.scaffold.metawidget.MetawidgetInspectorFacade;
import org.jboss.forge.addon.scaffold.metawidget.inspector.ForgeInspectionResultConstants;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.Member;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.metawidget.inspector.InspectionResultConstants;
import org.metawidget.util.simple.StringUtils;

/**
 * An 'Inspection Result Processor' that enhances the inspection results provided by Metawidget. This class does not
 * implement the {@link org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor} of Metawidget since it
 * subverts the Metawidget pipeline.
 *
 * This processor enhances the inspection results with HTML form labels for the inspected properties. It canonicalizes
 * all numerical types to the HTML5 'number' form input type.
 *
 * It also prompts the user to choose a field to be displayed in the HTML form select fields. Form select fields may
 * display Ids, but this may not be intuitive, especially when other properties would be better suited visually.
 */
public class InspectionResultProcessor
{

   private MetawidgetInspectorFacade metawidgetInspectorFacade;
   private Project project;

   public InspectionResultProcessor(Project project, MetawidgetInspectorFacade metawidgetInspectorFacade)
   {
      this.project = project;
      this.metawidgetInspectorFacade = metawidgetInspectorFacade;
   }

   public List<Map<String, String>> enhanceResults(JavaClassSource entity, List<Map<String, String>> inspectionResults)
   {
      Iterator<Map<String, String>> iterInspectionResults = inspectionResults.iterator();
      List<Map<String, String>> additionalPropertyAttributes = new ArrayList<>();
      while (iterInspectionResults.hasNext())
      {
         Map<String, String> propertyAttributes = iterInspectionResults.next();
         // FORGEPLUGINS-120 Omit references to classes having composite keys
         if (shouldOmitPropertyWithCompositeKey(propertyAttributes))
         {
            iterInspectionResults.remove();
            continue;
         }
         // Expand @Embedded properties
         List<Map<String, String>> expandedPropertyAttributes = expandEmbeddableTypes(propertyAttributes);
         if (expandedPropertyAttributes != null)
         {
            additionalPropertyAttributes.addAll(expandedPropertyAttributes);
            iterInspectionResults.remove();
            continue;
         }
      }
      inspectionResults.addAll(additionalPropertyAttributes);
      for (Map<String, String> propertyAttributes : inspectionResults)
      {
         createJavaScriptIdentifiers(propertyAttributes);
         populateLabelStrings(propertyAttributes);
         canonicalizeTypes(propertyAttributes);
         canonicalizeTemporalTypes(propertyAttributes);
         chooseRelationshipOptionLabels(entity, propertyAttributes);
      }
      return inspectionResults;
   }

   /**
    * Provides the Id of the JPA entity as obtained during inspection by Metawidget.
    *
    * @param entity The {@link org.jboss.forge.roaster.model.JavaClass} representing the JPA entity.
    * @param inspectionResults A list representing the inspection results for each property of the entity
    * @return The name of the property in the entity representing the entity {@link javax.persistence.Id} aka the
    *         primary key.
    */
   public String fetchEntityId(JavaClass<?> entity, List<Map<String, String>> inspectionResults)
   {
      for (Map<String, String> inspectionResult : inspectionResults)
      {
         boolean isPrimaryKey = Boolean.parseBoolean(inspectionResult.get(ForgeInspectionResultConstants.PRIMARY_KEY));
         if (isPrimaryKey)
         {
            return inspectionResult.get(NAME);
         }
      }
      throw new IllegalStateException("No Id was found for the class:" + entity.getName());
   }

   private List<Map<String, String>> expandEmbeddableTypes(Map<String, String> propertyAttributes)
   {
      String isEmbeddableAttribute = propertyAttributes.get(ForgeInspectionResultConstants.EMBEDDABLE);
      boolean isEmbeddable = isEmbeddableAttribute != null
               && isEmbeddableAttribute.equals(InspectionResultConstants.TRUE);
      if (isEmbeddable)
      {
         String embeddedType = propertyAttributes.get(TYPE);
         JavaClassSource javaClass = getJavaClass(embeddedType);
         List<Map<String, String>> embeddedTypeInspectionResults = metawidgetInspectorFacade.inspect(javaClass);
         List<Map<String, String>> expandedInspectionResults = new ArrayList<>();
         for (Map<String, String> embeddedPropertyAttribute : embeddedTypeInspectionResults)
         {
            embeddedPropertyAttribute.put(LABEL, StringUtils.uncamelCase(embeddedPropertyAttribute.get(NAME)));
            embeddedPropertyAttribute.put(TYPE, embeddedPropertyAttribute.get(TYPE));
            embeddedPropertyAttribute.put(JS_IDENTIFIER,
                     propertyAttributes.get(NAME) + StringUtils.camelCase(embeddedPropertyAttribute.get(NAME)));
            embeddedPropertyAttribute.put(NAME,
                     propertyAttributes.get(NAME) + "." + embeddedPropertyAttribute.get(NAME));
            expandedInspectionResults.add(embeddedPropertyAttribute);
         }
         return expandedInspectionResults;
      }
      return null;
   }

   private void createJavaScriptIdentifiers(Map<String, String> propertyAttributes)
   {
      String javascriptIdentifier = propertyAttributes.get(JS_IDENTIFIER);
      if (javascriptIdentifier == null)
      {
         propertyAttributes.put(JS_IDENTIFIER, propertyAttributes.get(NAME));
      }
   }

   private void populateLabelStrings(Map<String, String> propertyAttributes)
   {
      String propertyName = propertyAttributes.get(NAME);
      if (propertyAttributes.get(LABEL) == null)
      {
         propertyAttributes.put(LABEL, StringUtils.uncamelCase(propertyName));
      }
   }

   private void canonicalizeTypes(Map<String, String> propertyAttributes)
   {
      // Canonicalize all numerical types in Java to "number" for HTML5 form input type support
      String propertyType = propertyAttributes.get(TYPE);
      if (propertyType.equals(short.class.getName()) || propertyType.equals(int.class.getName())
               || propertyType.equals(long.class.getName()) || propertyType.equals(float.class.getName())
               || propertyType.equals(double.class.getName()) || propertyType.equals(Short.class.getName())
               || propertyType.equals(Integer.class.getName()) || propertyType.equals(Long.class.getName())
               || propertyType.equals(Float.class.getName()) || propertyType.equals(Double.class.getName()))
      {
         propertyAttributes.put(TYPE, "number");
      }
      if (propertyType.equals(boolean.class.getName()) || propertyType.equals(Boolean.class.getName()))
      {
         propertyAttributes.put(TYPE, "boolean");
         propertyAttributes.remove(LOOKUP);
      }
   }

   private void canonicalizeTemporalTypes(Map<String, String> propertyAttributes)
   {
      // Canonicalize all temporal types in Java to "temporal" for easier handling of date/time properties
      String type = propertyAttributes.get(TYPE);
      String datetimeType = propertyAttributes.get(DATETIME_TYPE);
      if (type.equals(Date.class.getName()) && datetimeType == null)
      {
         propertyAttributes.put("temporal", "true");
         propertyAttributes.put(DATETIME_TYPE, "both");
      }
      if (datetimeType != null
               && (datetimeType.equals("date") || datetimeType.equals("time") || datetimeType.equals("both")))
      {
         propertyAttributes.put("temporal", "true");
      }
   }

   private void chooseRelationshipOptionLabels(JavaClassSource entity, Map<String, String> propertyAttributes)
   {
      // Extract simple type name of the relationship types
      boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
      boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
      boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
      if (isManyToOneRel || isNToManyRel || isOneToOneRel)
      {
         String rightHandSideType;
         // Obtain the class name of the other/right-hand side of the relationship.
         if (isOneToOneRel || isManyToOneRel)
         {
            rightHandSideType = propertyAttributes.get("type");
         }
         else
         {
            rightHandSideType = propertyAttributes.get("parameterized-type");
         }
         String rightHandSideSimpleName = getSimpleName(rightHandSideType);
         propertyAttributes.put("simpleType", rightHandSideSimpleName);
         JavaClassSource javaClass = getJavaClass(rightHandSideType);
         List<Map<String, String>> rhsInspectionResults = metawidgetInspectorFacade.inspect(javaClass);
         List<InspectedProperty> fieldsToDisplay = getPropertiesToDisplay(
                  getDisplayableProperties(rhsInspectionResults));
         InspectedProperty defaultField = fieldsToDisplay.size() > 0 ? fieldsToDisplay.get(0) : null;
         InspectedProperty fieldToDisplay = defaultField;
         /*
          * TODO Prompt users on what fields are to be used. InspectedProperty fieldToDisplay =
          * prompt.promptChoiceTyped("Which property of " + rightHandSideSimpleName + " do you want to display in the "
          * + entity.getName() + " views ?", fieldsToDisplay, defaultField);
          */
         propertyAttributes.put("optionLabel", fieldToDisplay.getName());
         propertyAttributes.put("option-label-temporal-type", fieldToDisplay.getTemporalType());
      }
   }

   private List<InspectedProperty> getPropertiesToDisplay(List<Map<String, String>> displayableProperties)
   {
      List<InspectedProperty> fieldsToDisplay = new ArrayList<>();
      for (Map<String, String> displayableProperty : displayableProperties)
      {
         fieldsToDisplay.add(new InspectedProperty(displayableProperty));
      }
      return fieldsToDisplay;
   }

   private boolean shouldOmitPropertyWithCompositeKey(Map<String, String> propertyAttributes)
   {
      // Extract simple type name of the relationship types
      boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
      boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
      boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
      if (isManyToOneRel || isNToManyRel || isOneToOneRel)
      {
         String rightHandSideType;
         // Obtain the class name of the other/right-hand side of the relationship.
         if (isOneToOneRel || isManyToOneRel)
         {
            rightHandSideType = propertyAttributes.get("type");
         }
         else
         {
            rightHandSideType = propertyAttributes.get("parameterized-type");
         }
         JavaClassSource javaClass = getJavaClass(rightHandSideType);
         for (Member<?> member : javaClass.getMembers())
         {
            // FORGEPLUGINS-120 Ensure that properties with composite keys are detected and omitted from generation
            if (member.hasAnnotation(Id.class) && !javaClass.hasAnnotation(IdClass.class))
            {
               return false;
            }
            if (member.hasAnnotation(EmbeddedId.class))
            {
               return true;
            }
         }
      }
      return false;
   }

   private class InspectedProperty
   {

      private Map<String, String> delegate;

      InspectedProperty(Map<String, String> displayableProperty)
      {
         this.delegate = displayableProperty;
      }

      public String getTemporalType()
      {
         return delegate.get(DATETIME_TYPE);
      }

      public String getName()
      {
         return delegate.get(NAME);
      }

      @Override
      public String toString()
      {
         return delegate.get(NAME);
      }
   }

   // TODO; Extract this method into it's own class, for unit testing.
   private List<Map<String, String>> getDisplayableProperties(List<Map<String, String>> inspectionResults)
   {
      List<Map<String, String>> displayableProperties = new ArrayList<>();
      for (Map<String, String> propertyAttributes : inspectionResults)
      {
         canonicalizeTypes(propertyAttributes);
         canonicalizeTemporalTypes(propertyAttributes);
         boolean isManyToOneRel = Boolean.parseBoolean(propertyAttributes.get("many-to-one"));
         boolean isOneToOneRel = Boolean.parseBoolean(propertyAttributes.get("one-to-one"));
         boolean isNToManyRel = Boolean.parseBoolean(propertyAttributes.get("n-to-many"));
         if (!isManyToOneRel && !isNToManyRel && !isOneToOneRel)
         {
            // Display only basic properties.
            String hidden = propertyAttributes.get("hidden");
            String required = propertyAttributes.get("required");
            boolean isHidden = Boolean.parseBoolean(hidden);
            boolean isRequired = Boolean.parseBoolean(required);
            if (!isHidden)
            {
               displayableProperties.add(propertyAttributes);
            }
            else if (isRequired)
            {
               // Do nothing if hidden, unless required
               displayableProperties.add(propertyAttributes);
            }
         }
      }

      // If no properties were found suitable for display, add the primary key instead
      if (displayableProperties.size() < 1)
      {
         for (Map<String, String> propertyAttributes : inspectionResults)
         {
            if (propertyAttributes.get(PRIMARY_KEY) != null)
            {
               displayableProperties.add(propertyAttributes);
            }
         }
      }

      return displayableProperties;
   }

   private String getSimpleName(String rightHandSideType)
   {
      return getJavaClass(rightHandSideType).getName();
   }

   private JavaClassSource getJavaClass(String qualifiedType)
   {
      JavaSourceFacet java = this.project.getFacet(JavaSourceFacet.class);
      try
      {
         JavaResource resource = java.getJavaResource(qualifiedType);
         JavaClassSource javaClass = resource.getJavaType();
         return javaClass;
      }
      catch (FileNotFoundException fileEx)
      {
         throw new RuntimeException(fileEx);
      }
   }

}
