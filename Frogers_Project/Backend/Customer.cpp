#include "Customer.h"

Customer::Customer(int id, QString firstname,QString lastname,QString email, QString password, QString role, QString phone, QString planid) : User(id,firstname,lastname,email,password,role)
{
    this->_phone=phone;
    this->_planid=planid;
}

void Customer::setPhone(QString phone){
    this->_phone = phone;
}

QString Customer::getPhone(){
    return this->_phone;
}

QString Customer::getPlanId(){
    return this->_planid;
}
