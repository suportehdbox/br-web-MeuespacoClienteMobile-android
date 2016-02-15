/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents an Event object in the database
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class Event
{
    private boolean anyoneInjured; // TODO EPO REMOVER!

    private Timestamp createDateTime;

    private ArrayList<Driver> drivers;

    private Timestamp eventDateTime;

    private String eventStatus;

    private String eventSubType;

    private String eventSubTypeDetails;

    private String eventType;

    private String claimNumber;

    private Long id;

    private Address location;

    private String notes;

    private ArrayList<EventPhoto> photos;

    private ArrayList<BoletimDeOcorrencia> boletimDeOcorrenciaList;

//    private String policyNumber;

    private int sortValue;

    private Timestamp submitDateTime;

    private Vehicle vehicleInvolved;

    private ArrayList<VoiceNote> voiceNotes;

    private ArrayList<Witness> witnesses;

    private User submittedUser;
    
    // << EPO
//    private User shareDataUser;
    // >>

    /**
     * @return the claimNumber
     */
    public String getClaimNumber()
    {
        return claimNumber;
    }

    /**
     * This method will return the number of photos in a section.
     * 
     * @param section
     *            The section to look in
     * @return count of photos
     */
    public int getCountOfPhotosInSection(int section)
    {
        if (getPhotos() == null)
        {
            return 0;
        }
        int count = 0;
        Iterator<EventPhoto> iterator = getPhotos().iterator();
        while (iterator.hasNext())
        {
            EventPhoto photo = iterator.next();
            if (photo.getImageSection() == section)
            {
                count++;
            }
        }

        return count;
    }

    /**
     * @return the createDateTime
     */
    public Timestamp getCreateDateTime()
    {
        return createDateTime;
    }

    /**
     * @return the drivers
     */
    public ArrayList<Driver> getDrivers()
    {
        return drivers;
    }

    /**
     * @return the eventDateTime
     */
    public Timestamp getEventDateTime()
    {
        return eventDateTime;
    }

    /**
     * @return the eventStatus
     */
    public String getEventStatus()
    {
        return eventStatus;
    }

    /**
     * @return the eventSubType
     */
    public String getEventSubType()
    {
        return eventSubType;
    }

    /**
     * @return the eventSubTypeDetails
     */
    public String getEventSubTypeDetails()
    {
        return eventSubTypeDetails;
    }

    /**
     * @return the eventType
     */
    public String getEventType()
    {
        return eventType;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return the activity_location
     */
    public Address getLocation()
    {
        return location;
    }

    /**
     * @return the activity_notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * @return the photos
     */
    public ArrayList<EventPhoto> getPhotos()
    {
        return photos;
    }

    /**
     * @return the list of BoletimDeOcorrencia
     */
    public ArrayList<BoletimDeOcorrencia> getPoliceInfos()
    {
        return boletimDeOcorrenciaList;
    }

//    /**
//     * @return the policyNumber
//     */
//    public String getPolicyNumber()
//    {
//        return policyNumber;
//    }

    /**
     * @return the sortValue
     */
    public int getSortValue()
    {
        return sortValue;
    }

    /**
     * @return the submitDateTime
     */
    public Timestamp getSubmitDateTime()
    {
        return submitDateTime;
    }

    /**
     * @return the submittedUser
     */
    public User getSubmittedUser()
    {
        return submittedUser;
    }

    /**
     * @return the vehicleInvolved
     */
    public Vehicle getVehicleInvolved()
    {
        return vehicleInvolved;
    }

    /**
     * @return the voiceNotes
     */
    public ArrayList<VoiceNote> getVoiceNotes()
    {
        return voiceNotes;
    }

    /**
     * @return the witnesses
     */
    public ArrayList<Witness> getWitnesses()
    {
        return witnesses;
    }

    /**
     * This method determines if the activity_location is valid.
     * 
     * @return false if no activity_location, or if all required fields are not filled in. Else true.
     */
    public boolean hasValidLocation()
    {
        boolean retVal = false;
        if (getLocation() != null)
        {
            if (!getLocation().isEmpty())
            {
                retVal = true;
            }
        }
        return retVal;
    }

    /**
     * @return the anyoneInjured
     */
    public boolean isAnyoneInjured()
    {
        return anyoneInjured;
    }

    /**
     * @param anyoneInjured
     *            the anyoneInjured to set
     */
    public void setAnyoneInjured(boolean anyoneInjured)
    {
        this.anyoneInjured = anyoneInjured;
    }

    /**
     * @param claimNumber
     *            the claimNumber to set
     */
    public void setClaimNumber(String claimNumber)
    {
        this.claimNumber = claimNumber;
    }

    /**
     * @param createDateTime
     *            the createDateTime to set
     */
    public void setCreateDateTime(Timestamp createDateTime)
    {
        this.createDateTime = createDateTime;
    }

    /**
     * @param drivers
     *            the drivers to set
     */
    public void setDrivers(ArrayList<Driver> drivers)
    {
        this.drivers = drivers;
    }

    /**
     * @param eventDateTime
     *            the eventDateTime to set
     */
    public void setEventDateTime(Timestamp eventDateTime)
    {
        this.eventDateTime = eventDateTime;
    }

    /**
     * @param eventStatus
     *            the eventStatus to set
     */
    public void setEventStatus(String eventStatus)
    {
        this.eventStatus = eventStatus;
    }

    /**
     * @param eventSubType
     *            the eventSubType to set
     */
    public void setEventSubType(String eventSubType)
    {
        this.eventSubType = eventSubType;
    }

    /**
     * @param eventSubTypeDetails
     *            the eventSubTypeDetails to set
     */
    public void setEventSubTypeDetails(String eventSubTypeDetails)
    {
        this.eventSubTypeDetails = eventSubTypeDetails;
    }

    /**
     * @param eventType
     *            the eventType to set
     */
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
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
     * @param location
     *            the activity_location to set
     */
    public void setLocation(Address location)
    {
        this.location = location;
    }

    /**
     * @param notes
     *            the activity_notes to set
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * @param photos
     *            the photos to set
     */
    public void setPhotos(ArrayList<EventPhoto> photos)
    {
        this.photos = photos;
    }

    /**
     * @param BoletimDeOcorrencia
     *            the BoletimDeOcorrencia to set
     */
    public void setPoliceInfos(ArrayList<BoletimDeOcorrencia> policeInfos)
    {
        this.boletimDeOcorrenciaList = policeInfos;
    }

//    /**
//     * @param policyNumber
//     *            the policyNumber to set
//     */
//    public void setPolicyNumber(String policyNumber)
//    {
//        this.policyNumber = policyNumber;
//    }

    /**
     * @param sortValue
     *            the sortValue to set
     */
    public void setSortValue(int sortValue)
    {
        this.sortValue = sortValue;
    }

    /**
     * @param submitDateTime
     *            the submitDateTime to set
     */
    public void setSubmitDateTime(Timestamp submitDateTime)
    {
        this.submitDateTime = submitDateTime;
    }

    /**
     * @param submittedUser
     *            the submittedUser to set
     */
    public void setSubmittedUser(User submittedUser)
    {
        this.submittedUser = submittedUser;
    }

    /**
     * @param vehicleInvolved
     *            the vehicleInvolved to set
     */
    public void setVehicleInvolved(Vehicle vehicleInvolved)
    {
        this.vehicleInvolved = vehicleInvolved;
    }

    /**
     * @param voiceNotes
     *            the voiceNotes to set
     */
    public void setVoiceNotes(ArrayList<VoiceNote> voiceNotes)
    {
        this.voiceNotes = voiceNotes;
    }

    /**
     * @param witnesses
     *            the witnesses to set
     */
    public void setWitnesses(ArrayList<Witness> witnesses)
    {
        this.witnesses = witnesses;
    }

//	public User getShareDataUser() {
//		return shareDataUser;
//	}
//
//	/**
//	 * @param shareDataUser
//	 *            the user (share data) to set
//	 */
//	public void setShareDataUser(User shareDataUser) {
//		this.shareDataUser = shareDataUser;
//	}
    
    
}
