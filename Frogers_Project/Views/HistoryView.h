#ifndef HISTORYVIEW_H
#define HISTORYVIEW_H

#include <Controllers/HistoryViewController.h>
#include <QDialog>
#include <QSqlQueryModel>
#include <QSortFilterProxyModel>
#include <QStandardItemModel>
#include <vector>

/**
 * \class HistoryView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the History View Window.
 *
 *  Displays a table with all the past versions of changes made
 *  to a ticket. These include: Category, Severity, Status, and Assigned To.
 *  Allows the user to select any two tickets he/she wants to see only
 *  the changes from those selected tickets.
 *
 */

namespace Ui {
class HistoryView;
}

class HistoryView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * @brief HistoryView Constructor that initializes the ticket id
     * @param parent Parent class
     * @param ticketID Ticket number
     */
    explicit HistoryView(QWidget *parent = 0, QString ticketID = "");

    /**
     * \brief destructor
     */
    ~HistoryView();

    /**
     * \brief parse through the audit trail description to extract the data
     *
     * \param description the data containing all the changes made on the ticket
     * \return a vector containing all the data
     */
    std::vector<QString> parseDescription(QString description);

    /**
     * \brief shows all the rows
     */
    void showRows();

    /**
     * \brief hides the rows they are not checked
     */
    void hideRows();
    
public slots:

    /**
     * \brief updates the table when a checkbox is checked.
     */
    void updateTable(QModelIndex index);

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief the views controller
     */
    HistoryViewController* _controller;

    /**
     * \brief ui
     */
    Ui::HistoryView *ui;

    //
    //  MODEL CLASSES USED
    //

    /**
     * \brief model used to store data into the table
     */
    QSqlQueryModel* _model1;

    /**
     * \brief sorting proxy filter wrapper model
     */
    QSortFilterProxyModel *_model2;

    /**
     * \brief _changes stored changes of the ticket
     */
    QStandardItemModel *Model;

    /**
     * \brief tracks how many check boxes are checked
     */
    int checkedBoxes;

    /**
     * \brief number of columns in the table needed to show the data
     */
    int columnCount;

    /**
     * \brief number of rows in the table needed to show the data
     */
    int rowCount;

    /**
     * \brief keeps track of which boxes are checked and which are not checked.
     */
    std::vector<int> checkedStatus;
};

#endif // HISTORYVIEW_H
