#include "Controllers/EditCustomerController.h"
#include <QSqlQuery>
#include <QDebug>
#include <QSqlError>
#include <QRegExp>
//GOOD COPY

EditCustomerController::EditCustomerController()
{
    this->_db = DatabaseController::getInstance();
}

void EditCustomerController::InjectCustomer(int c){
    QSqlQuery customerQuery = this->_db->getCustomerByID(c);
    customerQuery.first();
    Customer* customer = new Customer(customerQuery.value(0).toInt(), customerQuery.value(1).toString(), customerQuery.value(2).toString(), customerQuery.value(3).toString(), customerQuery.value(4).toString(), customerQuery.value(5).toString(), customerQuery.value(6).toString(), customerQuery.value(7).toString());
    this->_customer = customer;
}

bool EditCustomerController::validate(QString test){
    if(test.isEmpty() || test.isNull())
        return false;
    return true;
}

void EditCustomerController::saveCustomer(Customer* c){
    //write to database with this method
    this->_db->saveCustomer(c);
}

std::vector<QString> EditCustomerController::getCustomerInfo(){

    //put values of currently selected customer into vector for easy access
    std::vector<QString> array;
    array.push_back(QString::number((_customer->getId())));
    array.push_back(_customer->getFirstName());
    array.push_back( _customer->getLastName());
    array.push_back(_customer->getEmail());
    array.push_back(_customer->getPassword());
    array.push_back(_customer->getRole());
    array.push_back(_customer->getPhone());
    array.push_back(_customer->getPlanId());
return array;
}

bool EditCustomerController::validEmail(QString email){
    QRegExp testEmail("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$");
    return testEmail.exactMatch(email);
}

bool EditCustomerController::validPhone(QString phone){

    //eliminate special characters
    QString number = phone.replace("(","");
    number = number.replace(")","");
    number = number.replace(".","");
    number = number.replace("(","");
    number = number.replace("-","");
    number = number.replace(".","");
    number = number.replace(" ","");

    //ensure there is no more than 1 x
    int extension = number.count('x');
    if(extension>1)
        return false;

    number = number.replace("x","");

    if(number.length()<7)
        return false;

    //ensure only numbers remain
    QRegExp testNumber("^[0-9]+$");
    return testNumber.exactMatch(number);
}

bool EditCustomerController::emailExists(QString email, QString oldEmail){
    return (this->_db->emailExists(email)) && (oldEmail.compare(email)!=0);
}
