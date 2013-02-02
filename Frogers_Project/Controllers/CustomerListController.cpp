#include "Controllers/CustomerListController.h"
#include "Backend/DatabaseController.h"
#include "ui_CustomerListView.h"
#include "QSqlDatabase"
#include "QSqlQuery"
#include <sstream>
#include <QString>
#include <QDebug>

CustomerListController::CustomerListController()
{
    this->_controller = DatabaseController::getInstance();

    this->_inappropriate.push_back("ruby");
    this->_inappropriate.push_back("pointer");
    this->_inappropriate.push_back("fail");
    this->_inappropriate.push_back("study");
    this->_inappropriate.push_back("exam");
    this->_inappropriate.push_back("test");
    this->_inappropriate.push_back("reference");
    this->_inappropriate.push_back("design");
    this->_inappropriate.push_back("folks");
    this->_inappropriate.push_back("shantz");
    this->_inappropriate.push_back("class");
    this->_inappropriate.push_back("binary");
    this->_inappropriate.push_back("tree");
    this->_inappropriate.push_back("vector");
    this->_inappropriate.push_back("homework");
    this->_inappropriate.push_back("sobriety");
}

// Loads the customer list
//
// \param currentIndex - The index of the current customer in the database
// \param offset - Determines the customers to display from the current index

QSqlQueryModel* CustomerListController::loadCustomers(QString currentIndex, QString offset, QString subscription){
            //get specified set of customers
            QSqlQuery query = this->_controller->loadCustomersbyFilters(currentIndex, offset, subscription);

            //return model to place into table
            QSqlQueryModel* model = new QSqlQueryModel();
            model->setQuery(query);

            return model;

}

int CustomerListController::resultCount(){
    return this->_controller->getCountResults(1);
}

int CustomerListController::customerCount(){
    return this->_controller->getCustomerCount();
}

QSqlQueryModel* CustomerListController::search(int offset,QString firstname,QString lastname,QString subscription,int index){
    //delete list of auto complete options
    this->_wordList.clear();
    //get set of customers based on parameters
    QSqlQuery query = this->_controller->search(firstname,lastname,offset,subscription,index);
    QSqlQueryModel* model = new QSqlQueryModel();
    model->setQuery(query);

    QString name;
    //add all found results to auto completer
    while(query.next())
    {
        name = query.value(0).toString()+" "+query.value(1).toString();
        this->_wordList<<name;
    }
    return model;

}

QStringList CustomerListController::getWordList(){
    return this->_wordList;
}

bool CustomerListController::isInappropriate(QString check){
    // checks if passed in work is considered "innappropriate"
    QString current;
    for(int i=0;i<this->_inappropriate.size();i++){
        current = this->_inappropriate.at(i);
        if(current.compare(check)==0)
            return true;
    }
    return false;

}

