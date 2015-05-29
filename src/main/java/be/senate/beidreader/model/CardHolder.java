package be.senate.beidreader.model;

/**
 * Created by wv on 26/05/2015.
 */

import be.belgium.eid.eidlib.BeID;
import be.belgium.eid.exceptions.EIDException;
import be.belgium.eid.objects.IDAddress;
import be.belgium.eid.objects.IDData;
import be.belgium.eid.objects.IDPhoto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Objects;


/**
 * Created by wv on 29/04/2015.
 */
public class CardHolder {
    private String regNr = "";
    private String country = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String language = "";
    private String function = "";
    private String picture = "";
    private String surName = "";
    private String name = "";
    private String extendedId = "";
    private String street = "";
    private String number = "";
    private String zipCode = "";
    private String city = "";
    private String adressCountry = "";
    private String birthDate = "";
    private String email = "";
    private String internalId = "";
    private String mobilePhone = "";
    private String officePhone = "";
    private String sexe = "";

    public CardHolder() {
    }

    public String getRegNr() {
        return regNr;
    }

    public void setRegNr(String regNr) {
        this.regNr = regNr;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    // This methode is used in producing an instance of javafx.scene.image.Image
    public InputStream getPictureAsInputStream() {
        Base64.Decoder decoder = Base64.getMimeDecoder();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.picture.getBytes());
        return decoder.wrap(byteArrayInputStream);
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendedId() {
        return extendedId;
    }

    public void setExtendedId(String extendedId) {
        this.extendedId = extendedId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdressCountry() {
        return adressCountry;
    }

    public void setAdressCountry(String adressCountry) {
        this.adressCountry = adressCountry;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void readBeID(BeID beID) throws EIDException {
        IDData idData = beID.getIDData();
        System.out.println("IDData gelezen...");
        String naam = idData.getName();
        this.lastName = naam;
        String voornaam1 = idData.get1stFirstname();
        String[] voornamen1 = voornaam1.split(" ");
        this.firstName = voornamen1[0];
        this.middleName = (voornamen1.length > 1 ) ? voornamen1[1] : "";
        String voornaam3 = idData.get3rdFirstname();
        String nationality = idData.getNationality();
        this.country = nationality;
        String rrn = idData.getNationalNumber();
        this.regNr = rrn;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDate = simpleDateFormat.format(idData.getBirthDate());
        this.birthDate = birthDate;
        String sexe = String.valueOf(idData.getSex());
        this.sexe = sexe;
        IDAddress idAddress = beID.getIDAddress();
        System.out.println("IDAddress gelezen...");
        String fullStreet = idAddress.getStreet();
        String[] streetParts = fullStreet.split(" ");
        if (streetParts.length <= 1) {
            this.street = fullStreet;
        } else {
            this.number = streetParts[streetParts.length - 1];
            this.street = streetParts[0];
            if (streetParts.length > 2) {
                for (int i = 1; i < streetParts.length - 1; i++)
                    this.street = this.street + " " + streetParts[i];
            }
        }
        this.city = idAddress.getMunicipality();
        this.zipCode = idAddress.getZipCode();
        IDPhoto idPhoto = beID.getIDPhoto();
        System.out.println("IDPhoto gelezen...");
        byte[] imageAsBytes = idPhoto.getPhoto();
        System.out.println("getPhoto gedaan...");
        Base64.Encoder encoder = Base64.getMimeEncoder(-1, new byte[0]);
        this.picture = encoder.encodeToString(imageAsBytes);
        return;
    }

    public String toCsv() {
        String csvString = "CH" +
                "," + "\"" + regNr + "\"" +
                "," + "\"" + country + "\"" +
                "," + "\"" + firstName + "\"" +
                "," + "\"" + middleName + "\"" +
                "," + "\"" + lastName + "\"" +
                "," + "\"" + language + "\"" +
                "," + "\"" + function + "\"" +
                "," + "\"" + picture + "\"" +
                "," + "\"" + surName + "\"" +
                "," + "\"" + name + "\"" +
                "," + "\"" + extendedId + "\"" +
                "," + "\"" + street + "\"" +
                "," + "\"" + number + "\"" +
                "," + "\"" + zipCode + "\"" +
                "," + "\"" + city + "\"" +
                "," + "\"" + adressCountry + "\"" +
                "," + "\"" + birthDate + "\"" +
                "," + "\"" + email + "\"" +
                "," + "\"" + internalId + "\"" +
                "," + "\"" + mobilePhone + "\"" +
                "," + "\"" + officePhone + "\"" +
                "," + "\"" + sexe + "\"";
        return csvString;
    }

    public static CardHolder getInstanceFromCsv(String csvString) {
        String[] components = csvString.split(",");
        CardHolder cardHolder = new CardHolder();
        if (components.length != 23) {
            System.out.println((csvString.length() < 10) ? csvString : csvString.substring(0, 10) + "... :This csv-line has not the required number (23) of fields.");
        } else {
            cardHolder.regNr = normalizeString(components[1]);
            cardHolder.country = normalizeString(components[2]);
            cardHolder.firstName = normalizeString(components[3]);
            cardHolder.middleName = normalizeString(components[4]);
            cardHolder.lastName = normalizeString(components[5]);
            cardHolder.language = normalizeString(components[6]);
            cardHolder.function = normalizeString(components[7]);
            cardHolder.picture = normalizeString(components[8]);
            cardHolder.surName = normalizeString(components[9]);
            cardHolder.name = normalizeString(components[10]);
            cardHolder.extendedId = normalizeString(components[11]);
            cardHolder.street = normalizeString(components[12]);
            cardHolder.number = normalizeString(components[13]);
            cardHolder.zipCode = normalizeString(components[14]);
            cardHolder.city = normalizeString(components[15]);
            cardHolder.adressCountry = normalizeString(components[16]);
            cardHolder.birthDate = normalizeString(components[17]);
            cardHolder.email = normalizeString(components[18]);
            cardHolder.internalId = normalizeString(components[19]);
            cardHolder.mobilePhone = normalizeString(components[20]);
            cardHolder.officePhone = normalizeString(components[21]);
            cardHolder.sexe = normalizeString(components[22]);
        }
        return cardHolder;
    }

    // This private methode is used to peel of quote's if they are present in the beginning and the end
    private static String normalizeString(String rawString) {
        String normalizedString = rawString.startsWith("\"") ? rawString.substring(1, rawString.length()-1) : rawString;
        return normalizedString;
    }

    @Override
    public String toString() {
        return regNr + " " + lastName + " " + firstName;
    }


}

