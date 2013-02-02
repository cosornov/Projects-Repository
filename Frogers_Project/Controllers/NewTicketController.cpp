#include "Controllers/NewTicketController.h"
#include "Backend/Ticket.h"
#include <QDateTime>
#include "QSqlDatabase"
#include "QSqlQuery"
#include <QString>
#include <QDebug>


using std::map;
using std::pair;

NewTicketController::NewTicketController()
{
    this->_db = DatabaseController::getInstance();
}

bool NewTicketController::createTicket(int customerId, int categoryID, int severityID, QString subject, QString description, QDateTime created_at)
{
    int id = _db->getTicketCount() + 1; // ticket id
    int currentAgent = _db->getAgent(); // get current user id
    QString status = "Pending";
    QDateTime closed_at;
    Ticket t1(id, customerId, categoryID, severityID, currentAgent, status, subject, description, created_at, closed_at);

    bool value = this->_db->insertTicket(t1);
    return value;



}
//authenticate the customer
bool NewTicketController::authenticate(int id)
{


        QString idString =  QString::number(id); // covert id into string
        QSqlQuery query(this->_db->getDataBase());
        query.prepare("SELECT id FROM users WHERE id = :idString and role='customer' ");
        query.bindValue(":idString", idString);
        if (!query.exec())
        {
            qFatal("Unable to execute query.");
            return false;
        }
        query.next();
        //ensure id in databse matches id of customer
        if( query.value(0).toInt() !=0 && query.value(0).toInt() == id) // 0 means that the query is empty
            return true;
        else
            return false;
}

map<QString,int>  NewTicketController::getSeverityMap()
{
    _severityMap = this->_db->getSeverityMap();

    return _severityMap;
}
map<QString,int>  NewTicketController::getCategoryMap()
{


    _categoryMap = this->_db->getCategoryMap();

    return _categoryMap;
}
