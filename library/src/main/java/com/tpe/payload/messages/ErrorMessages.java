package com.tpe.payload.messages;

public class ErrorMessages {

    public static final String FIELD_EMPTY = "Please fill in at least one filter";
    public static final String USER_NOT_FOUND = "User does not exist with email %s";
    public static final String USER_NOT_FOUND_BY_ID = "User does not exist with id %s";
    public static final String BOOK_NOT_FOUND = "Book does not exist with id %s";
    public static final String LOAN_NOT_FOUND = "Loan does not exist with id %s";
    public static final String BOOK_ALREADY_EXISTS = "Book already exists with isbn %s";
    public static final String USER_ALREADY_EXISTS = "User already exists with email %s";
    public static final String AUTHOR_NOT_FOUND = "Authors does not exists";
    public static final String PUBLISHER_NOT_FOUND = "Publisher does not exists with id %s";
    public static final String PUBLISHER_NOT_FOUND_WITH_NAME = "Publisher does not exists with name %s";
    public static final String CATEGORY_NOT_FOUND = "Category does not exists with id %s";
    public static final String ROLE_DOES_NOT_EXIST = "This role %s is not supported";
    public static final String ROLE_NOT_FOUND = "This role does not exist";
    public static final String SAVE_USER_ERROR = "You are not authorized to create user with role %s";
    public static final String LOAN_NOT_AVAILABLE = "Books are not available for loaning ";
    public static final String USER_CAN_NOT_LOAN_BOOK = "User with id %s already has a book needs to be returned";
    public static final String OVER_MAX_BOOK_COUNT = "User can not loan over %s books";


}
