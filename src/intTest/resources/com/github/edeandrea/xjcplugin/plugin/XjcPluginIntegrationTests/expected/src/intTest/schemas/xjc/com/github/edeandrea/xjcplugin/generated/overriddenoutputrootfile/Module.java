//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.04 at 02:26:25 PM EST 
//


package com.github.edeandrea.xjcplugin.generated.overriddenoutputrootfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for module complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="module"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}baseBuildBean"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="artifacts" type="{}artifact" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dependencies" type="{}dependency" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="excludedArtifacts" type="{}artifact" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "module", propOrder = {
    "artifacts",
    "dependencies",
    "excludedArtifacts",
    "id"
})
public class Module
    extends BaseBuildBean
    implements Serializable
{

    @XmlElement(nillable = true)
    protected List<Artifact> artifacts;
    @XmlElement(nillable = true)
    protected List<Dependency> dependencies;
    @XmlElement(nillable = true)
    protected List<Artifact> excludedArtifacts;
    protected String id;

    /**
     * Gets the value of the artifacts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the artifacts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArtifacts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Artifact }
     * 
     * 
     */
    public List<Artifact> getArtifacts() {
        if (artifacts == null) {
            artifacts = new ArrayList<Artifact>();
        }
        return this.artifacts;
    }

    /**
     * Gets the value of the dependencies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependencies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependencies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dependency }
     * 
     * 
     */
    public List<Dependency> getDependencies() {
        if (dependencies == null) {
            dependencies = new ArrayList<Dependency>();
        }
        return this.dependencies;
    }

    /**
     * Gets the value of the excludedArtifacts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excludedArtifacts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcludedArtifacts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Artifact }
     * 
     * 
     */
    public List<Artifact> getExcludedArtifacts() {
        if (excludedArtifacts == null) {
            excludedArtifacts = new ArrayList<Artifact>();
        }
        return this.excludedArtifacts;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
