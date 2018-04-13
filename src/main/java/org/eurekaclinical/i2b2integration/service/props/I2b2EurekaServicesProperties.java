package org.eurekaclinical.i2b2integration.service.props;

/*
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import javax.inject.Singleton;
import org.eurekaclinical.standardapis.props.CasJerseyEurekaClinicalProperties;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class I2b2EurekaServicesProperties extends CasJerseyEurekaClinicalProperties {

    public I2b2EurekaServicesProperties() {
        super("/etc/ec-i2b2-integration");
    }

    @Override
    public String getProxyCallbackServer() {
        String result = getValue("eurekaclinical.i2b2service.callbackserver");
        if (result != null) {
            return result;
        } else {
            return getValue("eurekaclinical.i2b2integrationservice.callbackserver");
        }
    }

    public String getEurekaServiceUrl() {
        return getValue("eureka.services.url");
    }

    public String getSourceConfigId() {
        String result = getValue("eurekaclinical.i2b2service.sourceConfigId");
        if (result != null) {
            return result;
        } else {
            return getValue("eurekaclinical.i2b2integrationservice.sourceConfigId");
        }
    }

    public String getUserAgreementServiceUrl() {
        return getValue("eurekaclinical.useragreementservice.url");
    }

    public String getCasEmailAttribute() {
        return getValue("cas.attribute.email");
    }

    @Override
    public String getUrl() {
        return getValue("eurekaclinical.i2b2integrationservice.url");
    }

}
