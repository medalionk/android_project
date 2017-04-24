package ee.ut.demo.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bilal Abdullah on 4/23/2017.
 */

public class PlaceDetail implements Parcelable {

    private final String id;
    private final LatLng location;
    private final String name;
    private final String placeId;
    private final String reference;

    protected PlaceDetail(Parcel in) {
        id = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        name = in.readString();
        placeId = in.readString();
        reference = in.readString();
    }

    public static final Creator<PlaceDetail> CREATOR = new Creator<PlaceDetail>() {
        @Override
        public PlaceDetail createFromParcel(Parcel in) {
            return new PlaceDetail(in);
        }

        @Override
        public PlaceDetail[] newArray(int size) {
            return new PlaceDetail[size];
        }
    };

    public String getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getReference() {
        return reference;
    }

    private PlaceDetail(Builder builder){
        id = builder.id;
        location = builder.location;
        name = builder.name;
        placeId = builder.placeId;
        reference = builder.reference;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(location, 0);
        dest.writeString(name);
        dest.writeString(placeId);
        dest.writeString(reference);
    }

    public static class Builder {

        private String id;
        private LatLng location;
        private String name;
        private String placeId;
        private String reference;

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder location(LatLng location) {
            this.location = location;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder placeId(String placeId) {
            this.placeId = placeId;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public PlaceDetail build() {
            return new PlaceDetail(this);
        }
    }
}
