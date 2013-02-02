#ifndef CUSTOMERCONTROLLER_H
#define CUSTOMERCONTROLLER_H

#include "Backend/DatabaseController.h"
#include "QSqlQueryModel"
#include <QString>
#include <vector>
#include <QStringList>

/**
 * \class CustomerListController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief The CustomerListController class Class that acts as a controller for the CustomerList
 */


class CustomerListController
{
public:

    /**
     * \brief CustomerListController Constructor that generates a new CustomerListController
     */
    CustomerListController();

    /**
     * \brief loadCustomers Method that gets a specified set of customers from the database
     * \param startIndex where to start searching
     * \param endIndex end of search index
     * \param subscription filter by subscription plan
     * \return model of specified customers to put into table
     */
    QSqlQueryModel* loadCustomers(QString startIndex, QString endIndex, QString subscription);

    /**
     * \brief customerCount gets count of customers from the database
     * \return number of customers
     */
    int customerCount();

    /**
     * \brief resultCount Returns the number of results
     * \return number of results
     */
    int resultCount();

    /**
     * \brief search searches for customers based on the criteria
     * \param offset where to start the search
     * \param firstname first name of customer
     * \param lastname last name of customer
     * \param subscription subscription plan if provided
     * \param index index value of sarch
     * \return
     */
    QSqlQueryModel* search(int offset,QString firstname,QString lastname,QString subscription,int index);

    /**
     * \brief getWordList gets list of words that match auto complete
     * \return QStringList
     */
    QStringList getWordList();

    /**
     * \brief isInappropriate verifies if passed word does not work
     * \param check value to compare against inappropriate words
     * \return true if inappropriate
     */
    bool isInappropriate(QString check);

private:

    /**
     * \brief _controller Reference to the Frogers database.
     *
     */
    DatabaseController *_controller;

    /**
     * @brief _wordList list of words thatt much auto complete
     */
    QStringList _wordList;

    /**
     * @brief _inappropriate Vector containing inappropriate words
     */
    std::vector<QString> _inappropriate;
};

#endif // CUSTOMERCONTROLLER_H
