package com.theminimalistdev.springbootecommerce.config;

import com.theminimalistdev.springbootecommerce.entity.Product;
import com.theminimalistdev.springbootecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] unsupported = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        //disable HTTP methods for Product: PUT, POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(unsupported)))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupported));

        //disable HTTP methods for ProductCategory: PUT, POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(unsupported)))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupported));

        // call an internal helper method to help us expose the id's
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        List<Class> entityClasses = new ArrayList<>();

        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        //expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
        config.exposeIdsFor(domainTypes);

    }
}
