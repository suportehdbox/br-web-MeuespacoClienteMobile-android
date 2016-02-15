/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

/**
 * Represents a VoiceNote object from the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class VoiceNote
{

    private Long id;

    private Integer lengthOfVoiceNote;

    private String pathToVoiceNote;

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the lengthOfVoiceNote
     */
    public Integer getLengthOfVoiceNote()
    {
        return lengthOfVoiceNote;
    }

    /**
     * @return the pathToVoiceNote
     */
    public String getPathToVoiceNote()
    {
        return pathToVoiceNote;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @param lengthOfVoiceNote
     *            the lengthOfVoiceNote to set
     */
    public void setLengthOfVoiceNote(Integer lengthOfVoiceNote)
    {
        this.lengthOfVoiceNote = lengthOfVoiceNote;
    }

    /**
     * @param pathToVoiceNote
     *            the pathToVoiceNote to set
     */
    public void setPathToVoiceNote(String pathToVoiceNote)
    {
        this.pathToVoiceNote = pathToVoiceNote;
    }
}
