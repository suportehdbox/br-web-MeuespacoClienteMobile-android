/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

/**
 * Represents and EventPhoto object in the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class EventPhoto implements Comparable<EventPhoto>
{
    private Long id;

    private int imagePosition;

    private int imageSection;

    private String photoPath;

    private String thumbnailPath;

    /**
     * @param another
     * @return
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(EventPhoto another)
    {
        if (getImageSection() != another.getImageSection())
        {
            return getImageSection() - another.getImageSection();
        }

        return getImagePosition() - another.getImagePosition();
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the imagePosition
     */
    public int getImagePosition()
    {
        return imagePosition;
    }

    /**
     * @return the imageSection
     */
    public int getImageSection()
    {
        return imageSection;
    }

    /**
     * @return the photo
     */
    public String getPhotoPath()
    {
        return photoPath;
    }

    /**
     * @return the thumbnail
     */
    public String getThumbnailPath()
    {
        return thumbnailPath;
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
     * @param imagePosition
     *            the imagePosition to set
     */
    public void setImagePosition(int imagePosition)
    {
        this.imagePosition = imagePosition;
    }

    /**
     * @param imageSection
     *            the imageSection to set
     */
    public void setImageSection(int imageSection)
    {
        this.imageSection = imageSection;
    }

    /**
     * @param photoPath
     *            the photo to set
     */
    public void setPhotoPath(String photoPath)
    {
        this.photoPath = photoPath;
    }

    /**
     * @param thumbnailPath
     *            the thumbnail to set
     */
    public void setThumbnailPath(String thumbnailPath)
    {
        this.thumbnailPath = thumbnailPath;
    }
}
