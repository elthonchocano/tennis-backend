package com.echocano.tennis.league.domain.model;

import java.security.SecureRandom;

public class Player {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String hand; // 'R' or 'L'
    private String email;
    private String oauth2Provider = "LOCAL";
    private String oauth2Id;
    private String avatarUrl;
    private String invitationCode;

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public Player() {
    }

    public Player(Long id, String phoneNumber, String firstName, String lastName, String hand, String email,
            String oauth2Provider, String oauth2Id, String avatarUrl, String invitationCode) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hand = hand;
        this.email = email;
        this.oauth2Provider = oauth2Provider;
        this.oauth2Id = oauth2Id;
        this.avatarUrl = avatarUrl;
        this.invitationCode = invitationCode;
    }

    public void linkGoogleAccount(String googleId, String googleAvatar) {
        if ("LOCAL".equals(this.oauth2Provider)) {
            this.oauth2Provider = "GOOGLE";
            this.oauth2Id = googleId;
            this.avatarUrl = googleAvatar;
        }
    }

    public void initializeLocalPlayer() {
        this.oauth2Provider = "LOCAL";
        this.invitationCode = generateRandomCode();
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauth2Provider() {
        return oauth2Provider;
    }

    public void setOauth2Provider(String oauth2Provider) {
        this.oauth2Provider = oauth2Provider;
    }

    public String getOauth2Id() {
        return oauth2Id;
    }

    public void setOauth2Id(String oauth2Id) {
        this.oauth2Id = oauth2Id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

}