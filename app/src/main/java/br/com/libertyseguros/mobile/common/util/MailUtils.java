/*
 * Copyright (c) 2011, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.common.util;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.model.Address;
import br.com.libertyseguros.mobile.model.BoletimDeOcorrencia;
import br.com.libertyseguros.mobile.model.Contact;
import br.com.libertyseguros.mobile.model.Driver;
import br.com.libertyseguros.mobile.model.Event;
import br.com.libertyseguros.mobile.model.EventPhoto;
import br.com.libertyseguros.mobile.model.Policy;
import br.com.libertyseguros.mobile.model.User;
import br.com.libertyseguros.mobile.model.Vehicle;
import br.com.libertyseguros.mobile.model.Witness;

/**
 * @author N0053575 (Heidi Sturm)
 */
public final class MailUtils
{
    /**
     * The logging tag
     */
    // private static final String TAG = MailUtils.class.getName();

    /**
     * This method will get the geolocation formatted for an email
     * 
     * @param spannableStringBuilder
     *            SpannableStringBuilder to append the information to
     * @param latitude
     *            latitude to append
     * @param longitude
     *            longitude to append
     */
    protected static void appendGeoLocation(SpannableStringBuilder spannableStringBuilder, String latitude,
        String longitude)
    {
        spannableStringBuilder.append(getFormattedProperty("Latitude: ", "" + latitude));
        spannableStringBuilder.append(getFormattedProperty("Longitude: ", "" + longitude));
        spannableStringBuilder
            .append(getFormattedProperty("Google Maps Link: ", getGoogleMapsUrl(latitude, longitude)));
    }

    /**
     * This method will create the <li>tag, given a particular photo
     * 
     * @param spannableStringBuilder
     *            ssb to append the info to
     * @param section
     *            section the photo belongs to
     * @param count
     *            number in sequence that this photo is in it's particular section
     * @param eventType
     *            event line of business
     */
    protected static void appendPhoto(SpannableStringBuilder spannableStringBuilder, int section, int count,
        String eventType)
    {
        spannableStringBuilder.append(Html.fromHtml("<li>"));
        String attachmentName = getAttachmentName(section, count, eventType);
        spannableStringBuilder.append(Html.fromHtml("&nbsp;&nbsp;&nbsp;" + attachmentName + "<br />"));
        spannableStringBuilder.append(Html.fromHtml("</li>"));
    }

    /**
     * Creates the body of an email given a header, footer and list of labels and values.
     * 
     * @param header
     *            the text to display at the beginning of the email
     * @param properties
     *            the list of key value pairs to display as the main body of the email. The key will become the label
     *            and will be bolded an proceeded by a colon. The value will be immediately after the label in normal
     *            text. Each pair will appear on a new line.
     * @return
     */
    public static SpannableStringBuilder buildEmailBody(String header, LinkedHashMap<String, String> properties)
    {
        // Log.v(TAG, ">>> buildEmailBody(String header, LinkedHashMap<String, String> properties, String footer)");

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (header != null && header.length() > 0)
        {
            builder.append(header + "<br /><br />");
        }

        builder.append(buildEmailPropertyList(properties));

        builder.append(getMyInfoEmailFooter(properties.get("VERSION_NAME"), properties.get("VERSION_NUMBER")));

        // Log.v(TAG, "<<< buildEmailBody(String header, LinkedHashMap<String, String> properties, String footer)");

        return builder;
    }

    /**
     * Builds the html necessary to display a list of key value pairs as labels and values, each on their own line, with
     * the label being bolded and proceeded by a colon, and the value appearing immediately after.
     * 
     * @param properties
     *            the list of key value pairs to build from
     * @return
     */
    private static SpannableStringBuilder buildEmailPropertyList(LinkedHashMap<String, String> properties)
    {
        // Log.v(TAG, ">>> SpannableStringBuilder buildEmailPropertyList(LinkedHashMap<String, String> properties)");

        SpannableStringBuilder builder = new SpannableStringBuilder();

        Iterator<Entry<String, String>> it = properties.entrySet().iterator();

        while (it.hasNext())
        {
            Entry<String, String> property = it.next();
            if (!property.getKey().startsWith("VERSION_"))
            {
                builder.append("<b>" + property.getKey() + ":</b>\t\t" + property.getValue() + "<br />");
            }
        }

        // Log.v(TAG, " SpannableStringBuilder buildEmailPropertyList(LinkedHashMap<String, String> properties)");

        return builder;
    }

    /**
     * Because the photos are stored on the filesystem, and not as blobs in the database, and the fact that we used the
     * photo id as the filename, we must rename the photos to attach them. We do not want to name them in the filesystem
     * as they are named here, because we would get collisions and we would have to rename the files as they are
     * deleted. This method now does a copy instead of a rename...this is because not all mail clients loaded the files,
     * prior to them being renamed back to the original name.
     * 
     * @param eventType
     *            Auto or Home
     * @param sectionCounts
     *            used to create target file name
     * @param cacheDir
     *            Directory to copy to
     * @param photo
     *            photo to copy
     */
    public static void copyImage(String eventType, int[] sectionCounts, String cacheDir, EventPhoto photo)
    {
        sectionCounts[photo.getImageSection()]++;

        File currentName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + photo.getPhotoPath());
        StringBuffer newPath = new StringBuffer();
        newPath.append(Environment.getExternalStorageDirectory().getAbsolutePath() + "");
        newPath.append(cacheDir);
        if (Constants.LOB_AUTO.equals(eventType))
        {
            newPath.append(getAutoAttachmentName(photo.getImageSection(), sectionCounts[photo.getImageSection()]));
        }
        File newName = new File(newPath.toString());
        FileUtils.copyFile(currentName, newName);

        // Log.v(TAG, "Copied " + currentName + " to " + newName);
    }

    /**
     * @param spannableStringBuilder
     * @param address
     */
    private static void getAddressText(SpannableStringBuilder spannableStringBuilder, Address address)
    {
        if (address != null)
        {
        	// TODO EPO: pegar texto em pt-br
            spannableStringBuilder.append(getFormattedProperty("Local/Endereço do Acidente: ", address.getStreetAddress()));
            spannableStringBuilder.append(getFormattedProperty("Cidade: ", address.getCity()));
//            spannableStringBuilder.append(getFormattedProperty("State: ", address.getState()));
            spannableStringBuilder.append(getFormattedProperty("CEP: ", address.getZipCode()));
        }
    }

    /**
     * @param section
     * @param count
     * @param eventType
     * @return attachmentName
     */
    private static String getAttachmentName(int section, int count, String eventType)
    {
        String attachmentName = null;
        if (Constants.LOB_AUTO.equals(eventType))
        {
            attachmentName = getAutoAttachmentName(section, count);
        }
        return attachmentName;
    }

    /**
     * @param section
     * @param count
     * @return attachmentName
     */
    private static String getAutoAttachmentName(int section, int count)
    {
        String attachmentName = null;
        switch (section)
        {
            case 0:
                attachmentName = "O-Seu-Veiculo-" + count + ".jpg";
                break;
            case 1:
                attachmentName = "Outros-Veiculos-" + count + ".jpg";
                break;
            case 2:
                attachmentName = "Local-Do-Acidente" + count + ".jpg";
                break;
            case 3:
                attachmentName = "Documentos-" + count + ".jpg";
                break;
            default:
                break;
        }
        return attachmentName;
    }

    /**
     * This method will return the body of the email
     * 
     * @param event
     *            Event to use in the email
     * @return
     */
    public static Spannable getBodyText(Event event, User user, String version, String build)
    {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        getPolicyHolderText(event, user, spannableStringBuilder);

        getClaimDetailsText(event, user, spannableStringBuilder);

        if (Constants.LOB_AUTO.equals(event.getEventType()))
        {

            Vehicle vehicle = event.getVehicleInvolved();
            if (vehicle != null)
            {
                spannableStringBuilder.append(Html.fromHtml("<br /><br /><b>Vehicle Involved</b><br /><br />"));
                spannableStringBuilder.append(getFormattedProperty("Year: ", vehicle.getYear()));
                spannableStringBuilder.append(getFormattedProperty("Make: ", vehicle.getMake()));
                spannableStringBuilder.append(getFormattedProperty("Model: ", vehicle.getModel()));
                spannableStringBuilder.append(getFormattedProperty("Color: ", vehicle.getColor()));
                spannableStringBuilder.append(getFormattedProperty("Plate Number: ", vehicle.getRegistrationNumber()));
                spannableStringBuilder.append(getFormattedProperty("Plate State: ", vehicle.getRegistrationState()));
            }

            getWitnessText(event, spannableStringBuilder);

            getOtherDriverText(event, spannableStringBuilder);

            getPoliceText(event, spannableStringBuilder);
        }

        getPhotoText(event, spannableStringBuilder);
 
//        getInjuryText(event, spannableStringBuilder);

        spannableStringBuilder.append("\n\n" + getEmailFooter(version, build));

        return spannableStringBuilder;
    }

    /**
     * @param event
     * @param user
     * @param spannableStringBuilder
     */
    private static void getClaimDetailsText(Event event, User user, SpannableStringBuilder spannableStringBuilder)
    {
        spannableStringBuilder.append(Html.fromHtml("<br /><br /><b>Detalhes do Sinistro</b><br /><br />"));
        
        // EPO - Alteração, chamado não mostrava numero da apolice no e-mail de envio de sinistro!!
//        Policy policy = user.getAutoPolicy();
        String policyNumber = "";
//        if (policy != null) {
//            policyNumber = policy.getPolicyNumber();
//        }
        if(null != event.getClaimNumber()){
            policyNumber = event.getClaimNumber();      	
        }
        
        
//        spannableStringBuilder.append(getFormattedProperty("Apólice # ", PolicyNumberUtils.formatPolicyNumberForDisplay(policyNumber)));
        spannableStringBuilder.append(getFormattedProperty("Apólice # ", 		policyNumber));
        spannableStringBuilder.append(getFormattedProperty("Tipo Sinistro: ", 	event.getEventSubType()));
//        if ((event.getEventSubTypeDetails() != null) && (!"".equals(event.getEventSubTypeDetails()))) {
//            spannableStringBuilder.append(getFormattedProperty("Claim Description: ", event.getEventSubTypeDetails()));
//        }
        getDateTimeText(spannableStringBuilder, event.getEventDateTime());
        if (Constants.LOB_AUTO.equals(event.getEventType()))
        {
            getAddressText(spannableStringBuilder, event.getLocation());
            Double latitude = event.getLocation().getLatitude();
            Double longitude = event.getLocation().getLongitude();

            if (latitude != 0 && longitude != 0) {
                appendGeoLocation(spannableStringBuilder, latitude.toString(), longitude.toString());
            }
        }

        spannableStringBuilder.append(getFormattedProperty("Notas: ", event.getNotes()));
    }

    /**
     * Adds the incident date/time to the stringBuilder
     * 
     * @param stringBuilder
     * @param ts
     */
    private static void getDateTimeText(SpannableStringBuilder stringBuilder, Timestamp ts)
    {
        if (ts != null)
        {
            stringBuilder.append(getFormattedProperty("Data Sinistro: ", getDate(ts.getTime())));
        }
    }

    /**
     * Formats a long as a date
     * 
     * @param longDate
     * @return
     */
    protected static String getDate(long longDate)
    {
        if (longDate == 0)
        {
            return "";
        }
        Date date = new Date(longDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
        return simpleDateFormat.format(date);
    }

    /**
     * This method will return the stander email footer.
     * 
     * @return
     */
    private static Spannable getEmailFooter(String version, String build)
    {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("Enviado do ");
        builder.append(Build.MANUFACTURER);
        builder.append(" ");
        builder.append(Build.MODEL);
        builder.append(" (");
        builder.append(Build.VERSION.RELEASE);
        builder.append(") Versão do App ");
        builder.append(version);
        builder.append(" ");
        builder.append(build);
        builder.append("\n");

        return builder;
    }

    /**
     * This method will return the stander email footer.
     * 
     * @return
     */
    private static Spannable getMyInfoEmailFooter(String version, String build)
    {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("<br><br>Enviado do Liberty Mobile - Android.\n");

        return builder;
    }
    /**
     * This method will format a property
     * 
     * @param l
     *            Label to use in the format
     * @param v
     *            Value to use in the format
     * @return
     */
    protected static Spannable getFormattedProperty(String l, String v)
    {
        String label;
        String value;

        if (l == null)
        {
            label = "";
        }
        else
        {
            label = l;
        }
        if (v == null)
        {
            value = "";
        }
        else
        {
            value = v;
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(Html.fromHtml(new StringBuilder("<b>").append(label).append("</b>").toString()));
        spannableStringBuilder.append(value);
        if (!value.endsWith("\n"))
        {
            spannableStringBuilder.append("\n");
        }

        return spannableStringBuilder;
    }

    /**
     * Formats a activity_location as a google maps url
     * 
     * @param latitude
     *            latitude of activity_location
     * @param longitude
     *            longitude of activity_location
     * @return
     */
    protected static String getGoogleMapsUrl(String latitude, String longitude)
    {
        return new StringBuilder("http://maps.google.com/maps?f=q&q=").append(latitude).append(",").append(longitude)
            .toString();
    }
//
//    /**
//     * @param section
//     * @param count
//     * @param attachmentName
//     * @return
//     */
//    private static String getHomeAttachmentName(int section, int count)
//    {
//        String attachmentName = null;
//        switch (section)
//        {
//            case 0:
//                attachmentName = "YourHome-" + count + ".jpg";
//                break;
//            case 1:
//                attachmentName = "IncidentSurroundings-" + count + ".jpg";
//                break;
//            case 2:
//                attachmentName = "PolicyDocuments-" + count + ".jpg";
//                break;
//            default:
//                break;
//        }
//        return attachmentName;
//    }
//    /**
//     * @param event
//     * @param spannableStringBuilder
//     */
//    private static void getInjuryText(Event event, SpannableStringBuilder spannableStringBuilder)
//    {
//        spannableStringBuilder.append(Html
//            .fromHtml("<br /><br /><b>Was anyone involved in the incident injured?</b><br /><br />"));
//        if (event.isAnyoneInjured())
//        {
//            spannableStringBuilder.append(Html.fromHtml("Yes"));
//        }
//        else
//        {
//            spannableStringBuilder.append(Html.fromHtml("No"));
//        }
//    }

    /**
     * @param event
     * @param spannableStringBuilder
     */
    private static void getOtherDriverText(Event event, SpannableStringBuilder spannableStringBuilder)
    {
        ArrayList<Driver> drivers = event.getDrivers();
        if (drivers != null)
        {
            Iterator<Driver> driverIterator = drivers.iterator();
            int driverCount = 1;
            while (driverIterator.hasNext())
            {
                Driver driver = driverIterator.next();
                spannableStringBuilder.append(Html.fromHtml("<br /><br /><b>Outros Condutores (" + driverCount++ + ")</b><br /><br />"));
                
                spannableStringBuilder.append(getFormattedProperty("Nome: ",		driver.getContact().getFirstName()));
                spannableStringBuilder.append(getFormattedProperty("Sobrenome: ", 	driver.getContact().getLastName()));
                spannableStringBuilder.append(getFormattedProperty("Telefone: ",	PhoneNumberUtils.formatPhoneNumberForDisplay(driver.getContact().getHomePhone())));
                spannableStringBuilder.append(getFormattedProperty("Email: ", 		driver.getContact().getEmailAddress()));
                spannableStringBuilder.append(getFormattedProperty("Seguradora: ", 	driver.getInsuranceCompany()));
                spannableStringBuilder.append(getFormattedProperty("Apólice: ",		driver.getPolicy().getPolicyNumber()));

                Policy policy = driver.getPolicy();
                if (policy != null)
                {
                    ArrayList<Vehicle> vehicleList = policy.getPolicyVehicles();

                    if (vehicleList != null && vehicleList.size() > 0)
                    {
                        Vehicle vehicle = vehicleList.get(0);
                        spannableStringBuilder.append(getFormattedProperty("Marca: ", 	vehicle.getMake()));
                        spannableStringBuilder.append(getFormattedProperty("Modelo: ",	vehicle.getModel()));
                        spannableStringBuilder.append(getFormattedProperty("Ano: ", 	vehicle.getYear()));
                        spannableStringBuilder.append(getFormattedProperty("Cor: ",		vehicle.getColor()));
                        spannableStringBuilder.append(getFormattedProperty("Placa: ", 	vehicle.getRegistrationNumber()));
//                        spannableStringBuilder.append(getFormattedProperty("License Plate State: ", 	vehicle.getRegistrationState()));
                    }
                }

                spannableStringBuilder.append(getFormattedProperty("Notes: ", driver.getContact().getNotes()));

            }
        }
    }

    /**
     * @param event
     * @param spannableStringBuilder
     */
    private static void getPhotoText(Event event, SpannableStringBuilder spannableStringBuilder)
    {
        ArrayList<EventPhoto> photos = event.getPhotos();
        if (photos != null)
        {
            Collections.sort(photos);
            Iterator<EventPhoto> photoIterator = photos.iterator();
            int photoCount = 0;
            int[] sectionCounts = new int[4];
            for (int i = 0; i < sectionCounts.length; i++)
            {
                sectionCounts[i] = 0;
            }

            spannableStringBuilder.append(Html.fromHtml("<br /><br />"));

            while (photoIterator.hasNext())
            {
                photoCount++;
                if (photoCount == 1)
                {
                    spannableStringBuilder.append(Html.fromHtml("Foram anexadas as seguintes fotografias:<br /><br />"));
                    spannableStringBuilder.append(Html.fromHtml("<ul>"));
                }
                EventPhoto photo = photoIterator.next();
                sectionCounts[photo.getImageSection()]++;
                appendPhoto(spannableStringBuilder, photo.getImageSection(), sectionCounts[photo.getImageSection()],
                    event.getEventType());
            }
            if (photoCount > 0)
            {
                spannableStringBuilder.append(Html.fromHtml("</ul>"));
            }
        }
    }

    /**
     * This method will return an ArrayList of the photos for the event.
     * 
     * @param event
     *            event to get photos from
     * @return ArrayList of photos
     */
    public static ArrayList<Uri> getPhotoUris(Event event, String cacheDir)
    {
        int[] sectionCounts = new int[4];
        for (int i = 0; i < sectionCounts.length; i++)
        {
            sectionCounts[i] = 0;
        }

        ArrayList<Uri> uris = new ArrayList<Uri>();
        ArrayList<EventPhoto> photos = event.getPhotos();
        if (photos != null)
        {
            Collections.sort(photos);
            Iterator<EventPhoto> photoIterator = photos.iterator();
            while (photoIterator.hasNext())
            {
                EventPhoto photo = photoIterator.next();
                sectionCounts[photo.getImageSection()]++;
                StringBuffer newPath = new StringBuffer();
                newPath.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                newPath.append(cacheDir);
                if (Constants.LOB_AUTO.equals(event.getEventType()))
                {
                    newPath.append(getAutoAttachmentName(photo.getImageSection(),
                        sectionCounts[photo.getImageSection()]));
                }
                File photoFile = new File(newPath.toString());
                uris.add(Uri.fromFile(photoFile));
            }
        }
        return uris;
    }

    /**
     * @param event
     * @param spannableStringBuilder
     */
    private static void getPoliceText(Event event, SpannableStringBuilder spannableStringBuilder)
    {
        ArrayList<BoletimDeOcorrencia> boletimDeOcorrenciaList = event.getPoliceInfos();
        if (boletimDeOcorrenciaList != null)
        {
            Iterator<BoletimDeOcorrencia> boletimDeOcorrenciaIterator = boletimDeOcorrenciaList.iterator();
            //For now, the GUI only allows one Police Information object to be entered.
            //int copperCount = 1;
            while (boletimDeOcorrenciaIterator.hasNext())
            {
            	BoletimDeOcorrencia copper = boletimDeOcorrenciaIterator.next();
                spannableStringBuilder.append(Html.fromHtml("<br /><br /><b>Informação Autoridade / B.O.</b><br /><br />"));
                spannableStringBuilder.append(getFormattedProperty("Entidade: ", copper.getEntidade()));
                spannableStringBuilder.append(getFormattedProperty("Localidade: ", copper.getLocalidade()));
                spannableStringBuilder.append(getFormattedProperty("Notas: ", copper.getNotas()));
            }
        }
    }

    /**
     * @param event
     * @param user
     * @param spannableStringBuilder
     */
    private static void getPolicyHolderText(Event event, User user, SpannableStringBuilder spannableStringBuilder)
    {
        Contact contact = event.getSubmittedUser().getContact();
        spannableStringBuilder.append(Html.fromHtml("<b>Informação do Segurado</b><br /><br />"));
        
        spannableStringBuilder.append(getFormattedProperty("Nome: ", 		contact.getFirstName()));
        spannableStringBuilder.append(getFormattedProperty("Sobrenome: ", 	contact.getLastName()));
        spannableStringBuilder.append(getFormattedProperty("Celular: ", 	PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getMobilePhone())));
        spannableStringBuilder.append(getFormattedProperty("Telefone: ", 	PhoneNumberUtils.formatPhoneNumberForDisplay(contact.getHomePhone())));
        spannableStringBuilder.append(getFormattedProperty("Email: ", 		contact.getEmailAddress()));

        // << EPO
//        String policyNumber = null;
//        String policyLOB = null;
//        if (Constants.LOB_AUTO.equals(event.getEventType()))
//        {
//            Policy policy = user.getAutoPolicy();
//            if (policy != null)
//            {
//                policyNumber = policy.getPolicyNumber();
//            }
//            else
//            {
//                policyNumber = "";
//            }
//            policyLOB = "Auto";
//        }
//        spannableStringBuilder.append(getFormattedProperty(policyLOB + " Policy #: ", PolicyNumberUtils.formatPolicyNumberForDisplay(policyNumber)));
        // >>

    }

    /**
     * This method will build a subject line
     * 
     * @param event
     *            The event to use in building the subject
     * @return
     */
    public static String getSubjectLine(Event event)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.getEventType());
        stringBuilder.append(" ");
        stringBuilder.append(event.getSubmittedUser().getContact().getLastName());
        stringBuilder.append(" ");
        stringBuilder.append(event.getSubmittedUser().getContact().getFirstName());
        stringBuilder.append(" Detalhe do Sinistro ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        stringBuilder.append(sdf.format(event.getCreateDateTime()));
        return stringBuilder.toString();
    }

    /**
     * @param event
     * @param spannableStringBuilder
     */
    private static void getWitnessText(Event event, SpannableStringBuilder spannableStringBuilder)
    {
        ArrayList<Witness> witnesses = event.getWitnesses();
        if (witnesses != null)
        {
            Iterator<Witness> witnessIterator = witnesses.iterator();
            int witnessCount = 1;
            while (witnessIterator.hasNext())
            {
                Contact witness = witnessIterator.next().getContact();
                spannableStringBuilder.append(Html.fromHtml("<br /><br /><b>Testemunhas (" + witnessCount++
                    + ")</b><br /><br />"));
                spannableStringBuilder.append(getFormattedProperty("Nome: ", witness.getFirstName()));
                spannableStringBuilder.append(getFormattedProperty("Sobrenome: ", witness.getLastName()));

                spannableStringBuilder.append(getFormattedProperty("Telefone: ",
                    PhoneNumberUtils.formatPhoneNumberForDisplay(witness.getHomePhone())));
                spannableStringBuilder.append(getFormattedProperty("Email: ", witness.getEmailAddress()));
                spannableStringBuilder.append(getFormattedProperty("Notas: ", witness.getNotes()));
            }
        }
    }

    /**
     * private constructor to prevent instantiation
     */
    private MailUtils()
    {
        super();
    }
}
