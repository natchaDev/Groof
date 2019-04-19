package th.co.octagoninteractive.groof.service.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import th.co.octagoninteractive.groof.repository.remote.dao.BillTotalDao;
import th.co.octagoninteractive.groof.repository.remote.dao.BillUpdateDao;
import th.co.octagoninteractive.groof.repository.remote.dao.BillYearlyDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ChangePasswordDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ContactDao;
import th.co.octagoninteractive.groof.repository.remote.dao.DocumentDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ForgottenDao;
import th.co.octagoninteractive.groof.repository.remote.dao.GraphDao;
import th.co.octagoninteractive.groof.repository.remote.dao.DashboardDataDao;
import th.co.octagoninteractive.groof.repository.remote.dao.LoginDao;
import th.co.octagoninteractive.groof.repository.remote.dao.NotificationItemsDao;
import th.co.octagoninteractive.groof.repository.remote.dao.NotificationListDao;
import th.co.octagoninteractive.groof.repository.remote.dao.NotificationReadDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ProfileDao;
import th.co.octagoninteractive.groof.repository.remote.dao.SiteDao;
import th.co.octagoninteractive.groof.repository.remote.dao.SiteListDao;
import th.co.octagoninteractive.groof.repository.remote.dao.ThemeDao;
import th.co.octagoninteractive.groof.repository.remote.dao.UpdateProfileDao;
import th.co.octagoninteractive.groof.repository.remote.dao.WeatherDao;
import th.co.octagoninteractive.groof.repository.remote.dao.WeatherListDao;

/**
 * Created by 8interactive 08/2018
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("power/dashboard")
    Call<DashboardDataDao> getDashboard(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );

    @FormUrlEncoded
    @POST("power/graph-daily")
    Call<GraphDao> getGraphDaily(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("power/graph-monthly")
    Call<GraphDao> getGraphMonthly(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("power/graph-yearly")
    Call<GraphDao> getGraphYearly(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("power/graph-total")
    Call<GraphDao> getGraphTotal(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id,
            @Field("date") String date
    );

    @GET("weather")
    Call<WeatherDao> getCurrentWeather(
            @Query("appid") String appid,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units
    );

    @GET("forecast")
    Call<WeatherListDao> getListWeather(
            @Query("appid") String appid,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units
    );

    @FormUrlEncoded
    @POST("project/data")
    Call<SiteDao> getSiteData(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );

    @GET("project/site")
    Call<SiteListDao> getSiteList(
            @Header("Authorization") String Authorization
    );

    @GET("customers/profile")
    Call<ProfileDao> getProfile(
            @Header("Authorization") String Authorization
    );


    @FormUrlEncoded
    @POST("customers/login")
    Call<LoginDao> UserLogin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("fcm_id") String fcm_id

    );

    @FormUrlEncoded
    @POST("bill/total")
    Call<BillTotalDao> getBillTotal(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );

    @FormUrlEncoded
    @POST("bill/yearly")
    Call<BillYearlyDao> getBillYearly(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("bill/updated")
    Call<BillUpdateDao> updatedBill(
            @Header("Authorization") String Authorization,
            @Field("bill_id") String bill_id,
            @Field("expense") String expense
    );

    @FormUrlEncoded
    @POST("customers/forgotpassword")
    Call<ForgottenDao> Forgotten(
            @Field("email") String email

    );

    @FormUrlEncoded
    @POST("project/theme")
    Call<ThemeDao> getTheme(
            @Field("H") String hr
    );

    @FormUrlEncoded
    @POST("notification/list")
    Call<NotificationListDao> getNotificationList(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );

    @FormUrlEncoded
    @POST("notification/read")
    Call<NotificationReadDao> getNotificationRead(
            @Header("Authorization") String Authorization,
            @Field("notification_id") String notification_id
    );

    @FormUrlEncoded
    @POST("notification/no-read")
    Call<NotificationItemsDao> getNotificationItems(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );

    @POST("customers/changepassword")
    Call<ChangePasswordDao> getChangePassword(
            @Header("Authorization") String Authorization,
            @Field("password") String password,
            @Field("newpassword") String newpassword,
            @Field("confirmpassword") String comfirmpassword
    );

    @FormUrlEncoded
    @POST("customers/updated")
    Call<UpdateProfileDao> getUpdateProfile(
            @Header("Authorization") String Authorization,
            @Field("name") String name,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("lineId") String lineID,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("customers/contact")
    Call<ContactDao> getContact(
            @Header("Authorization") String Authorization,
            @Field("subject") String subject,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("document/list")
    Call<DocumentDao> getDocumentList(
            @Header("Authorization") String Authorization,
            @Field("entity_id") String entity_id
    );
}
