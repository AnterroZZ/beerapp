package com.example.twojepiwo.LoginRegister;

import com.example.twojepiwo.Beers.Beers;

import java.util.Date;

public class User {
    private String Username;
    private String Password;
    private String EmailAddress;
    private Integer Weight;
    private Integer Height;
    private Integer Age;
    private String Sex;
    private String FavouriteBeer;
    private Integer Level;
    private Float LevelProgress;
    private String Title;
    private String DateOfCreation;
    private Integer TotalBeers;
    private Beers Beers;
    private Integer TodayBeers;

    public User()
    {

    }
    //Only for registration
    public User(String username, String password, String emailAddress,
                Integer weight, Integer height, Integer age, String sex)
    {
        this.Username = username;
        this.Password = password;
        this.EmailAddress = emailAddress;
        this.Weight = weight;
        this.Height = height;
        this.Age = age;
        this.Sex = sex;
        this.FavouriteBeer = "None";
        this.Level = 1;
        this.LevelProgress = 0F;
        this.Title = "New Comer";
        this.DateOfCreation = new Date().toString();
        this.TotalBeers = 0;
        this.Beers = new Beers();
        this.TodayBeers = 0;
    }

    public String getFavouriteBeer() {
        return FavouriteBeer;
    }

    public Integer getLevel() {
        return Level;
    }

    public Float getLevelProgress() {
        return LevelProgress;
    }

    public String getTitle() {
        return Title;
    }

    public String getDateOfCreation() {
        return DateOfCreation;
    }

    public void setFavouriteBeer(String favouriteBeer) {
        FavouriteBeer = favouriteBeer;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public void setLevelProgress(Float levelProgress) {
        LevelProgress = levelProgress;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDateOfCreation(String dateOfCreation) {
        DateOfCreation = dateOfCreation;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }

    public void setWeight(Integer weight) {
        this.Weight = weight;
    }

    public void setHeight(Integer height) {
        this.Height = height;
    }

    public void setAge(Integer age) {
        this.Age = age;
    }

    public void setSex(String sex) {
        this.Sex = sex;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public Integer getWeight() {
        return Weight;
    }

    public Integer getHeight() {
        return Height;
    }

    public Integer getAge() {
        return Age;
    }

    public String getSex() {
        return Sex;
    }

    public void setTotalBeers(Integer totalBeers) {
        TotalBeers = totalBeers;
    }

    public Integer getTotalBeers() {
        return TotalBeers;
    }

    public com.example.twojepiwo.Beers.Beers getBeers() {
        return Beers;
    }

    public void setBeers(com.example.twojepiwo.Beers.Beers beers) {
        Beers = beers;
    }

    public Integer getTodayBeers() {
        return TodayBeers;
    }

    public void setTodayBeers(Integer todayBeers) {
        TodayBeers = todayBeers;
    }
}
