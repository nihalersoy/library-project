package com.tpe.payload.messages;

public class ErrorMessages {

    public static final String FIELD_EMPTY = "Please fill in at least one filter";
    public static final String USER_NOT_FOUND = "User does not exist with email %s";
    public static final String BOOK_NOT_FOUND = "Book does not exist with id %s";
    public static final String BOOK_ALREADY_EXISTS = "Book already exists with isbn %s";
    public static final String USER_ALREADY_EXISTS = "User already exists with email %s";
    public static final String AUTHOR_NOT_FOUND = "Author does not exists with id %s";
    public static final String PUBLISHER_NOT_FOUND = "Publisher does not exists with id %s";
    public static final String CATEGORY_NOT_FOUND = "Category does not exists with id %s";
    public static final String ROLE_DOES_NOT_EXIST = "This role %s is not supported";
    public static final String ROLE_NOT_FOUND = "This role does not exist";
}
