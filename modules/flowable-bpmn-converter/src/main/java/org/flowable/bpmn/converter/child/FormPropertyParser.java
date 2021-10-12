/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.flowable.bpmn.converter.child;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.util.BpmnXMLUtil;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FormProperty;
import org.flowable.bpmn.model.FormValue;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;

import javax.xml.stream.XMLStreamReader;

/**
 * @author Tijs Rademakers
 */
public class FormPropertyParser extends BaseChildElementParser {

    @Override
    public String getElementName() {
        return ELEMENT_FORMPROPERTY;
    }

    @Override
    public boolean accepts(BaseElement element) {
        return ((element instanceof UserTask) || (element instanceof StartEvent));
    }

    @Override
    public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {

        if (!accepts(parentElement))
            return;

        FormProperty property = new FormProperty();
        BpmnXMLUtil.addXMLLocation(property, xtr);
        property.setId(xtr.getAttributeValue(null, ATTRIBUTE_FORM_ID));
        property.setName(xtr.getAttributeValue(null, ATTRIBUTE_FORM_NAME));
        property.setType(xtr.getAttributeValue(null, ATTRIBUTE_FORM_TYPE));
        property.setVisibilityExpression(xtr.getAttributeValue(null, ATTRIBUTE_FORM_VISIBILITYEXPRESSION));
        property.setVariable(xtr.getAttributeValue(null, ATTRIBUTE_FORM_VARIABLE));
        property.setExpression(xtr.getAttributeValue(null, ATTRIBUTE_FORM_EXPRESSION));
        property.setDefaultExpression(xtr.getAttributeValue(null, ATTRIBUTE_FORM_DEFAULT));
        property.setDatePattern(xtr.getAttributeValue(null, ATTRIBUTE_FORM_DATEPATTERN));
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_REQUIRED))) {
            property.setRequired(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_REQUIRED)));
        }
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_READABLE))) {
            property.setReadable(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_READABLE)));
        }
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_WRITABLE))) {
            property.setWriteable(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_WRITABLE)));
        }

        boolean readyWithFormProperty = false;
        try {
            while (!readyWithFormProperty && xtr.hasNext()) {
                xtr.next();
                if (xtr.isStartElement() && ELEMENT_VALUE.equalsIgnoreCase(xtr.getLocalName())) {
                    FormValue value = new FormValue();
                    BpmnXMLUtil.addXMLLocation(value, xtr);
                    value.setId(xtr.getAttributeValue(null, ATTRIBUTE_ID));
                    value.setName(xtr.getAttributeValue(null, ATTRIBUTE_NAME));
                    property.getFormValues().add(value);

                } else if (xtr.isEndElement() && getElementName().equalsIgnoreCase(xtr.getLocalName())) {
                    readyWithFormProperty = true;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error parsing form properties child elements", e);
        }

        if (parentElement instanceof UserTask) {
            ((UserTask) parentElement).getFormProperties().add(property);
        } else {
            ((StartEvent) parentElement).getFormProperties().add(property);
        }
    }
}
