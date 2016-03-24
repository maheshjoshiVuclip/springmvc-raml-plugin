package com.phoenixnap.oss.ramlapisync.generation.rules.spring;

import com.phoenixnap.oss.ramlapisync.data.ApiMappingMetadata;
import com.phoenixnap.oss.ramlapisync.data.ApiParameterMetadata;
import com.phoenixnap.oss.ramlapisync.generation.CodeModelHelper;
import com.phoenixnap.oss.ramlapisync.generation.rules.basic.MethodParamsRule;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JVar;
import org.raml.model.parameter.UriParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author armin.weisser
 * @since 0.3.2
 */
public class SpringMethodParamsRule extends MethodParamsRule {

    protected JVar param(ApiParameterMetadata paramMetaData, CodeModelHelper.JExtMethod generatableType) {
        JVar jVar = super.param(paramMetaData, generatableType);
        JAnnotationUse jAnnotationUse;
        if (paramMetaData.getRamlParam() != null && paramMetaData.getRamlParam() instanceof UriParameter) {
            jAnnotationUse = jVar.annotate(PathVariable.class);
        } else {
            jAnnotationUse = jVar.annotate(RequestParam.class);
        }

        // In RAML parameters are optional unless the required attribute is included and its value set to 'true'.
        // In Spring a parameter is required by default unlesse the required attribute is included and its value is set to 'false'
        // So we just need to set required=false if the RAML "required" parameter is not set or explicitly set to false.
        if(paramMetaData.getRamlParam() != null && !paramMetaData.getRamlParam().isRequired()) {
            jAnnotationUse.param("required", false);
        }

        return jVar;
    }

    @Override
    protected JVar param(ApiMappingMetadata endpointMetadata, CodeModelHelper.JExtMethod generatableType) {
        JVar param = super.param(endpointMetadata, generatableType);
        param.annotate(RequestBody.class);
        return param;
    }

}
