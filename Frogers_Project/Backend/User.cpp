#include "User.h"

User::User(int id, QString firstname,QString lastname,QString email, QString password, QString role) : DatabaseObject(id)
{
    this->_firstname=firstname;
    this->_lastname=lastname;
    this->_email=email;
    this->_password=password;
    this->_role=role;
}

const QString User::getFirstName(){
    return this->_firstname;
}

const QString User::getLastName(){
    return this->_lastname;
}

const QString User::getEmail(){
    return this->_email;
}

const QString User::getPassword(){
    return this->_password;
}

const QString User::getRole(){
    return this->_role;
}

void User::setFirstName(QString firstName){
    this->_firstname=firstName;
}

void User::setLastName(QString lastName){
    this->_lastname=lastName;
}

void User::setEmail(QString email){
    this->_email=email;
}

void User::setPassword(QString password){
    this->_password=password;
}

void User::setRole(QString role){
    this->_role=role;
}
