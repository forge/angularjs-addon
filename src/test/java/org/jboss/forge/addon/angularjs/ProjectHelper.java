/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.angularjs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_0;
import org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1;
import org.jboss.forge.addon.javaee.ejb.EJBFacet_3_2;
import org.jboss.forge.addon.javaee.faces.FacesFacet_2_2;
import org.jboss.forge.addon.javaee.jpa.JPAFacet_2_0;
import org.jboss.forge.addon.javaee.jpa.JPAFieldOperations;
import org.jboss.forge.addon.javaee.jpa.PersistenceOperations;
import org.jboss.forge.addon.javaee.servlet.ServletFacet_3_1;
import org.jboss.forge.addon.javaee.validation.ValidationFacet;
import org.jboss.forge.addon.parser.java.beans.FieldOperations;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.projects.JavaProjectType;
import org.jboss.forge.addon.parser.java.projects.JavaWebProjectType;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.Field;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;

/**
 * Helps with the configuration of a project
 *
 * @author <a href="ggastald@redhat.com">George Gastaldi</a>
 */
public class ProjectHelper
{
   @Inject
   private ProjectFactory projectFactory;

   @Inject
   private FacetFactory facetFactory;

   @Inject
   private JavaWebProjectType javaWebProjectType;

   @Inject
   private JavaProjectType javaProjectType;

   @Inject
   private PersistenceOperations persistenceOperations;

   @Inject
   private FieldOperations fieldOperations;

   @Inject
   private JPAFieldOperations jpaFieldOperations;

   /**
    * Creates a project installing the required facets from
    * {@link org.jboss.forge.addon.parser.java.projects.JavaWebProjectType#getRequiredFacets()}
    */
   public Project createWebProject()
   {
      return projectFactory.createTempProject(javaWebProjectType.getRequiredFacets());
   }

   /**
    * Creates a project installing the required facets from
    * {@link org.jboss.forge.addon.parser.java.projects.JavaProjectType#getRequiredFacets()}
    */
   public Project createJavaLibraryProject()
   {
      return projectFactory.createTempProject(javaProjectType.getRequiredFacets());
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.ejb.EJBFacet_3_2} facet
    */
   public EJBFacet_3_2 installEJB_3_2(Project project)
   {
      return facetFactory.install(project, EJBFacet_3_2.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.servlet.ServletFacet_3_1} facet
    */
   public ServletFacet_3_1 installServlet_3_1(Project project)
   {
      return facetFactory.install(project, ServletFacet_3_1.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.faces.FacesFacet_2_2} facet
    */
   public FacesFacet_2_2 installFaces_2_2(Project project)
   {
      return facetFactory.install(project, FacesFacet_2_2.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.jpa.JPAFacet} facet
    */
   public JPAFacet_2_0 installJPA_2_0(Project project)
   {
      return facetFactory.install(project, JPAFacet_2_0.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.cdi.CDIFacet_1_0} facet
    */
   public CDIFacet_1_0 installCDI_1_0(Project project)
   {
      return facetFactory.install(project, CDIFacet_1_0.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.cdi.CDIFacet_1_1} facet
    */
   public CDIFacet_1_1 installCDI_1_1(Project project)
   {
      return facetFactory.install(project, CDIFacet_1_1.class);
   }

   /**
    * Installs the {@link org.jboss.forge.addon.javaee.validation.ValidationFacet} facet
    */
   public ValidationFacet installValidation(Project project)
   {
      return facetFactory.install(project, ValidationFacet.class);
   }

   public JavaResource createJPAEntity(Project project, String entityName) throws IOException
   {
      String packageName = project.getFacet(JavaSourceFacet.class).getBasePackage() + ".model";
      return persistenceOperations.newEntity(project, entityName, packageName, GenerationType.AUTO);
   }

   public FieldSource<JavaClassSource> createStringField(JavaClassSource entityClass, String fieldName)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, String.class.getSimpleName(), fieldName);
   }

   public FieldSource<JavaClassSource> createBooleanField(JavaClassSource entityClass, String fieldName)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, boolean.class.getSimpleName(), fieldName);
   }

   public FieldSource<JavaClassSource> createBooleanWrapperField(JavaClassSource entityClass, String fieldName)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, Boolean.class.getSimpleName(), fieldName);
   }

   public Field<JavaClassSource> createTemporalField(JavaClassSource entityClass, String fieldName, TemporalType type)
            throws FileNotFoundException
   {
      FieldSource<JavaClassSource> field = fieldOperations.addFieldTo(entityClass, Date.class.getCanonicalName(),
               fieldName,
               TemporalType.class.getCanonicalName());
      field.addAnnotation(Temporal.class).setEnumValue(type);
      return field;
   }

   public FieldSource<JavaClassSource> createIntField(JavaClassSource entityClass, String fieldName)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, int.class.getName(), fieldName);
   }

   public FieldSource<JavaClassSource> createLongField(JavaClassSource entityClass, String fieldName)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, int.class.getName(), fieldName);
   }

   public FieldSource<JavaClassSource> createNumericField(JavaClassSource entityClass, String fieldName,
            Class<? extends Number> type)
            throws FileNotFoundException
   {
      return fieldOperations.addFieldTo(entityClass, type.getCanonicalName(), fieldName);
   }

   public void createOneToOneField(Project project, JavaResource javaResource, String fieldName,
            String type, String inverseFieldName, FetchType fetchType, boolean required,
            Iterable<CascadeType> cascadeTypes) throws FileNotFoundException
   {
      jpaFieldOperations.newOneToOneRelationship(project, javaResource, fieldName, type, inverseFieldName, fetchType,
               required, cascadeTypes);
   }

   public void createManyToOneField(Project project, JavaResource javaResource, String fieldName,
            String type, String inverseFieldName, FetchType fetchType, boolean required,
            Iterable<CascadeType> cascadeTypes) throws FileNotFoundException
   {
      jpaFieldOperations.newManyToOneRelationship(project, javaResource, fieldName, type, inverseFieldName, fetchType,
               required, cascadeTypes);
   }

   public void createOneToManyField(Project project, JavaResource javaResource, String fieldName,
            String type, String inverseFieldName, FetchType fetchType,
            Iterable<CascadeType> cascadeTypes) throws FileNotFoundException
   {
      jpaFieldOperations.newOneToManyRelationship(project, javaResource, fieldName, type, inverseFieldName, fetchType,
               cascadeTypes);
   }

   public void createManyToManyField(Project project, JavaResource javaResource, String fieldName,
            String type, String inverseFieldName, FetchType fetchType,
            Iterable<CascadeType> cascadeTypes) throws FileNotFoundException
   {
      jpaFieldOperations.newManyToManyRelationship(project, javaResource, fieldName, type, inverseFieldName, fetchType,
               cascadeTypes);
   }

   public void addNotNullConstraint(Project project, JavaClassSource klass, String propertyName, boolean onAccessor,
            String message)
            throws FileNotFoundException
   {
      PropertySource<JavaClassSource> property = klass.getProperty(propertyName);
      final Annotation<JavaClassSource> constraintAnnotation = addConstraintOnProperty(property, onAccessor,
               NotNull.class,
               message);

      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      javaSourceFacet.saveJavaSource(constraintAnnotation.getOrigin());
   }

   public void addMinConstraint(Project project, JavaClassSource klass, String propertyName, boolean onAccessor,
            String message, String min)
            throws FileNotFoundException
   {
      PropertySource<JavaClassSource> property = klass.getProperty(propertyName);
      final AnnotationSource<JavaClassSource> constraintAnnotation = addConstraintOnProperty(property, onAccessor,
               Min.class,
               message);
      constraintAnnotation.setLiteralValue(min);

      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      javaSourceFacet.saveJavaSource(constraintAnnotation.getOrigin());
   }

   public void addMaxConstraint(Project project, JavaClassSource klass, String propertyName, boolean onAccessor,
            String message, String max)
            throws FileNotFoundException
   {
      PropertySource<JavaClassSource> property = klass.getProperty(propertyName);
      final AnnotationSource<JavaClassSource> constraintAnnotation = addConstraintOnProperty(property, onAccessor,
               Max.class,
               message);
      constraintAnnotation.setLiteralValue(max);

      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      javaSourceFacet.saveJavaSource(constraintAnnotation.getOrigin());
   }

   public void addSizeConstraint(Project project, JavaClassSource klass, String propertyName, boolean onAccessor,
            String message,
            String min, String max) throws FileNotFoundException
   {
      PropertySource<JavaClassSource> property = klass.getProperty(propertyName);
      final AnnotationSource<JavaClassSource> constraintAnnotation = addConstraintOnProperty(property, onAccessor,
               Size.class,
               message);

      if (min != null)
      {
         constraintAnnotation.setLiteralValue("min", min);
      }

      if (max != null)
      {
         constraintAnnotation.setLiteralValue("max", max);
      }

      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      javaSourceFacet.saveJavaSource(constraintAnnotation.getOrigin());
   }

   private AnnotationSource<JavaClassSource> addConstraintOnProperty(PropertySource<JavaClassSource> property,
            boolean onAccessor,
            Class<? extends java.lang.annotation.Annotation> annotationClass, String message)
            throws FileNotFoundException
   {
      MemberSource<JavaClassSource, ?> member = property.getField();
      if (onAccessor)
      {
         final MethodSource<JavaClassSource> accessor = property.getAccessor();
         if (accessor == null)
         {
            throw new IllegalStateException("The property named '" + property.getName() + "' has no accessor");
         }
         member = accessor;
      }

      if (member.hasAnnotation(annotationClass))
      {
         throw new IllegalStateException("The element '" + member.getName() + "' is already annotated with @"
                  + annotationClass.getSimpleName());
      }

      AnnotationSource<JavaClassSource> annotation = member.addAnnotation(annotationClass);
      if (message != null)
      {
         annotation.setStringValue("message", message);
      }
      return annotation;
   }

}