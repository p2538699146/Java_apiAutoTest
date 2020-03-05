package com.autotest.pojo;

/**
 * @author shkstart
 * @create 2020-01-04-22:30
 */
public class Rest {
    private String API_ID;
    private String API_NAME;
    private String API_TYPE;
    private String URL;

    public String getAPI_ID() {
        return API_ID;
    }

    public String getAPI_NAME() {
        return API_NAME;
    }

    public String getURL() {
        return URL;
    }

    public void setAPI_ID(String API_ID) {
        this.API_ID = API_ID;
    }

    public void setAPI_NAME(String API_NAME) {
        this.API_NAME = API_NAME;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Rest() {
    }

    public void setAPI_TYPE(String API_TYEP) {
        this.API_TYPE = API_TYEP;
    }

    public String getAPI_TYPE() {
        return API_TYPE;
    }

    public Rest(String API_ID, String API_NAME, String API_TYPE, String URL) {
        this.API_ID = API_ID;
        this.API_NAME = API_NAME;
        this.API_TYPE = API_TYPE;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Rest{" +
                "API_ID='" + API_ID + '\'' +
                ", API_NAME='" + API_NAME + '\'' +
                ", API_TYEP='" + API_TYPE + '\'' +
                ", Url='" + URL + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rest rest = (Rest) o;

        if (API_ID != null ? !API_ID.equals(rest.API_ID) : rest.API_ID != null) return false;
        if (API_NAME != null ? !API_NAME.equals(rest.API_NAME) : rest.API_NAME != null) return false;
        if (API_TYPE != null ? !API_TYPE.equals(rest.API_TYPE) : rest.API_TYPE != null) return false;
        return URL != null ? URL.equals(rest.URL) : rest.URL == null;
    }

    @Override
    public int hashCode() {
        int result = API_ID != null ? API_ID.hashCode() : 0;
        result = 31 * result + (API_NAME != null ? API_NAME.hashCode() : 0);
        result = 31 * result + (API_TYPE != null ? API_TYPE.hashCode() : 0);
        result = 31 * result + (URL != null ? URL.hashCode() : 0);
        return result;
    }
}
