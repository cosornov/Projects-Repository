#include "HistoryViewController.h"
#include <QSqlQuery>
#include <QDebug>
#include <vector>

HistoryViewController::HistoryViewController()
{
     this->_db = DatabaseController::getInstance();
}

QSqlQueryModel* HistoryViewController::getTicketHistory(QString ticketID){

    QSqlQuery query = this->_db->getAuditTrail(ticketID);

    QSqlQueryModel* model = new QSqlQueryModel();
    model->setQuery(query);

    return model;
}

std::vector<QString> HistoryViewController::getValueNames(QString category, QString severity) {
    return this->_db->getNames(category, severity);
}
