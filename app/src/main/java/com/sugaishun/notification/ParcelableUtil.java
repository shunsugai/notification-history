package com.sugaishun.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUtil {

    private ParcelableUtil() {
        throw new AssertionError();
    }

    public static byte[] marshall(Parcelable parcelable) {
        if (parcelable == null) {
            throw new NullPointerException("parcelable cannot be null");
        }
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes cannot be null");
        }
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        if (bytes == null) {
            throw new NullPointerException("bytes cannot be null");
        }
        Parcel parcel = unmarshall(bytes);
        return creator.createFromParcel(parcel);
    }
}
