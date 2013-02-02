#ifndef CUSTOMERVIEW_H
#define CUSTOMERVIEW_H

#include <QMainWindow>
#include "Controllers/CustomerListController.h"
#include <QSortFilterProxyModel>

/**
 * \class CustomerListView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Customer List window.
 *
 *  Displays a table with all a specified pre-determined amount
 *  of customers (25) that can be mildy adjusted (10,25,50,100).
 *  Allows for subscription filtering, column sorting and customer
 *  searching.
 *
 *  Easter egg hidden in here as well :)
 */

namespace Ui {
class CustomerListView;
}

class CustomerListView : public QMainWindow
{
    Q_OBJECT

public slots:

    //
    // BUTTON METHODS
    //

    /**
     * \brief Actions performed once the load button is clicked. Loads number
     *        of customers specified.
     */
    void nextPageButtonClicked();

    /**
     * \brief Actions performed with previous clicked
     */
    void previousPageButtonClicked();

    /**
     * \brief Actions performed with last page button clicked
     */
    void lastPageButtonClicked();

    /**
     * \brief Actions performed with first page button clicked
     */
    void firstPageButtonClicked();

    //
    // WINDOW DATA METHODS - Deal with checking the validity and status of the buttons
    // and re-loading data when needed.
    //

    /**
     * \brief Actions performed when data needs refreshing
     */
    void updateData();

    /**
     * \brief Deals with button statuses
     *
     * Will check to see the current indexes and whether it is legal to
     * execute one of the four page buttons. If illegal, disable
     * the button otherwise enable the custom-style green button.
     */
    void updateButtonData();

    //
    // MISCELLANEOUS METHODS
    //

    /**
     * \brief actions performed when a customer needs to be edited
     */
    void editCustomer();

    /**
     * \brief search actions performed when searching is taking place
     */
    void search();

public:
    /**
     * \brief constructor for the main dashboard.
     *
     *\param parent QWidget
     */
    explicit CustomerListView(QMainWindow *parent = 0);

    /**
     * \brief destructor
     */
    ~CustomerListView();

    /**
     * \brief setButtonModel setup buttons
     */
    void setButtonModel();

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::CustomerListView *ui;

    /**
     * \brief _controller stores reference to personal controller
     */
    CustomerListController _controller;

    /**
     * \brief stores starting index which to reference tickets
     */
    int _currentIndex;

    /**
     * \brief the amount of tickets from the offset to obtain
     */
    int _offset;

    /**
     * \brief _inputBoxIndex stores index of input box
     */
    int _inputBoxIndex;

    /**
     * \brief _model1 stores query data to be inserted into a table view
     */
    QSqlQueryModel* _model1;

    /**
     * \brief _model2 acts a filter wrapper class for the query model
     */
    QSortFilterProxyModel *_model2;

    /**
     * \brief _greenButton stores data for making green buttons
     */
    QString _greenButton;
};

#endif // CUSTOMERVIEW_H
