#include "Controllers/DashboardController.h"
#include <QDebug>

DashboardController::DashboardController()
{
  this->_db = DatabaseController::getInstance();
}

// Loads the customer list
//
// \param currentIndex - The index of the current customer in the database
// \param offset - Determines the customers to display from the current index

QSqlQueryModel* DashboardController::loadTickets(QString currentIndex, QString offset){

            //get current list of tickets to place in table
            QSqlQuery query = this->_db->getTicketListData(currentIndex, offset);

            QSqlQueryModel* model = new QSqlQueryModel();
            model->setQuery(query);

            return model;
}
