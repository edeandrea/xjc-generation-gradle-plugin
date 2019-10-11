//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.11 at 11:19:33 AM EDT 
//


package com.github.edeandrea.xjcplugin.generated.artifactory;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseModel"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="feedbackMsg" type="{}feedbackMsg" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseModel", propOrder = {
    "feedbackMsg"
})
@XmlSeeAlso({
    XrayRepoModel.class,
    FeedbackMsg.class,
    GeneralTabLicenseModel.class,
    BaseArtifact.class,
    BaseSearchResult.class,
    UserProfileModel.class
})
public class BaseModel
    implements Serializable
{

    protected FeedbackMsg feedbackMsg;

    /**
     * Gets the value of the feedbackMsg property.
     * 
     * @return
     *     possible object is
     *     {@link FeedbackMsg }
     *     
     */
    public FeedbackMsg getFeedbackMsg() {
        return feedbackMsg;
    }

    /**
     * Sets the value of the feedbackMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link FeedbackMsg }
     *     
     */
    public void setFeedbackMsg(FeedbackMsg value) {
        this.feedbackMsg = value;
    }

}
