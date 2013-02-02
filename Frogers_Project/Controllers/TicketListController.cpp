#include "Controllers/TicketListController.h"
#include "ui_CustomerListView.h"
#include "QSqlDatabase"
#include "QSqlQuery"
#include <sstream>
#include <QString>
#include <QDebug>

TicketListController::TicketListController()
{
    this->_db = DatabaseController::getInstance();
}

QSqlQueryModel* TicketListController::loadTickets(QString currentIndex, QString offset){
    //get set of tickets to place into table
    QSqlQuery query = this->_db->getTicketListData(currentIndex, offset);
    QSqlQueryModel* model = new QSqlQueryModel();
    model->setQuery(query);

    return model;
}

QSqlQueryModel* TicketListController::loadTicketsbyFilters(QString currentIndex, QString offset, QString addedFilters [], QString addedFilterValues []){
    //get set of tickets with specific filters applied
    QSqlQuery query = this->_db->loadTicketsbyFilters(currentIndex, offset, addedFilters, addedFilterValues);
    //return the model to update table
    QSqlQueryModel* model = new QSqlQueryModel();
    model->setQuery(query);

    return model;
}

QString TicketListController::getAgentID() {
    return QString::number(this->_db->getAgent());
}


int TicketListController::resultCount(){
    return this->_db->getCountResults(0);
}


bool TicketListController ::update(QString category, QString severity, QString status, QString agentId, QString ticketId)
{
    return this->_db->updateTicket(category, severity, status, agentId,  ticketId);
}
bool TicketListController ::update(QString category, QString severity, QString status, QString agentId, QString closedDate, QString ticketId)
{
    return this->_db->updateTicket(category, severity, status, agentId, closedDate,  ticketId);
}

