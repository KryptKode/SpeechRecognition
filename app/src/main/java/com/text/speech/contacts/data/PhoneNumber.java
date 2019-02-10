package com.text.speech.contacts.data;

import android.provider.ContactsContract;

/**
 * Represents a phone number
 */
public class PhoneNumber {
    private final String number;
    private final Type type;
    private final String label;
    private final String normalizedNumber;



    public enum Type {
        CUSTOM,
        HOME,
        MOBILE,
        WORK,
        FAX_WORK,
        FAX_HOME,
        PAGER,
        OTHER,
        CALLBACK,
        CAR,
        COMPANY_MAIN,
        ISDN,
        MAIN,
        OTHER_FAX,
        RADIO,
        TELEX,
        TTY_TDD,
        WORK_MOBILE,
        WORK_PAGER,
        ASSISTANT,
        MMS,
        UNKNOWN;

        static Type fromValue(int value) {
            switch (value) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:
                    return CUSTOM;
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    return HOME;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    return MOBILE;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    return WORK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    return FAX_WORK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    return FAX_HOME;
                case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                    return PAGER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    return OTHER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                    return CALLBACK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                    return CAR;
                case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                    return COMPANY_MAIN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                    return ISDN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                    return MAIN;
                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                    return OTHER_FAX;
                case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                    return RADIO;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                    return TELEX;
                case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                    return TTY_TDD;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                    return WORK_MOBILE;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                    return WORK_PAGER;
                case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                    return ASSISTANT;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                    return MMS;
                default:
                    return UNKNOWN;
            }
        }
    }

    PhoneNumber(String number, Type type, String normalizedNumber) {
        this.number = number;
        this.type = type;
        this.label = null;
        this.normalizedNumber = normalizedNumber;
    }

    PhoneNumber(String number, String label, String normalizedNumber) {
        this.number = number;
        this.type = Type.CUSTOM;
        this.label = label;
        this.normalizedNumber = normalizedNumber;
    }

    /**
     * Gets the phone number.
     *
     * @return phone number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gets the label of the phone number. (null unless type = CUSTOM)
     *
     * @return label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the normalized phone number.
     *
     * @return normalized phone number.
     */
    public String getNormalizedNumber() {
        return normalizedNumber;
    }

    /**
     * Gets the type of the phone number.
     *
     * @return type.
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumber that = (PhoneNumber) o;

        return number.equals(that.number) &&  type == that.type &&
                !(label != null ? !label.equals(that.label) : that.label != null) &&
                !(normalizedNumber != null ? !normalizedNumber.equals(that.normalizedNumber) :
                        that.normalizedNumber != null);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (normalizedNumber != null ? normalizedNumber.hashCode() : 0);
        return result;
    }
}
