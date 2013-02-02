#ifndef TICKETLISTVIEW_H
#define TICKETLISTVIEW_H

/**
 * \class TicketListV
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Ticket List View and first view shown to the user upon logging in.
 *
 *  Displays a table with all a specified pre-determined amount
 *  of tickets (25) that can be mildy adjusted (10,25,50,100).
 *  Allows for multi-filtering, column sorting. Added refresh
 *  button as well to re-load fresh data.
 *
 *  Source for the custom css for the green button (e.g. page / login buttons etc...):
 *      Link: http://www.darrenhoyt.com/2009/09/20/better-button-and-nav-interactions/#
 *      Author Name: Darren Hoyt
 *      Date: December 2, 2012
 *      Code Type: CSS
 */

#include <QMainWindow>
#include "Controllers/TicketListController.h"
#include <QSortFilterProxyModel>
#include <QStandardItemModel>
#include <QStandardItem>
#include <QString>
#include <vector>
#include <QPoint>
#include <Views/EditTicketView.h>

namespace Ui {
class TicketListV;
}

class TicketListV : public QMainWindow
{
    Q_OBJECT

public slots:
    /**
     * Actions performed once the load button is clicked. Loads number
     * of customers specified.
     */
    void nextPageButtonClicked();

    /**
     * \brief actions performed when previous button clicked
     */
    void previousPageButtonClicked();

    /**
     * \brief actions performed when last page button clicked
     */
    void lastPageButtonClicked();

    /**
     * \brief actions performed when first page button clicked
     */
    void firstPageButtonClicked();

    /**
     * \brief actions performed when new ticket button clicked
     */
    void newTicketButtonClicked();

    /**
     * \brief actions performed when a customer needs to be edited
     */
    void editCus();

    /**
     * \brief actions performed when a right click menu should be opened
     */
    void rightClickMenu(const QPoint&);

    /**
     * \brief updates data
     */
    void updateData();

    /**
     * \brief updates data for buttons
     */
    void updateButtonData();

    /**
     * \brief editTicket actions performed when a ticket should be edited
     */
    void editTicket();

    /**
     * \brief undoClicked actions performed when undo is clicked
     */
    void undoClicked();


    /**
     * @brief appliedFiltersChanged actions performed when the filter is changed
     */
    void appliedFiltersChanged(const QModelIndex&, const QModelIndex&);


    /**
     * @brief showHideBoxChanged actions performed when a box is either hiddden or displayed
     */
    void showHideBoxChanged(const QModelIndex&);


    /**
     * @brief showFilter actions performed when a filter is selected. shows that filter tool
     * @param a index of the filter list
     */
    void showFilter(int a);


    /**
     * @brief applyFilter actions performed when a filter is applied
     */
    void applyFilter();

public:
    /**
     * Constructor for the main dashboard.
     *
     *\param parent QWidget
     */
    explicit TicketListV(QMainWindow *parent = 0);

    /**
     * \brief destructor
     */
    ~TicketListV();


    /**
     * @brief setButtonModel
     */
    void setButtonModel();

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::TicketListV *ui;

    /**
     * \brief _controller a reference to the personal controller
     */
    TicketListController _controller;


    /**
     * \brief stored starting index which to reference tickets
     */
    int _currentIndex;

    /**
     * \brief the amount of tickets from the offset to obtain
     */
    int _offset;

    /**
     * \brief keeps track of which boxes are checked and which are not checked.
     */
    int _inputBoxIndex;

    /**
     * \brief model used to store data into the table
     */
    QSqlQueryModel* _model1;

    /**
     * \brief sorting proxy filter wrapper model
     */
    QSortFilterProxyModel *_model2;

    /**
     * \brief the edit ticket view shown from the menu
     */
    EditTicketView *_ticketView;

    /**
     * \brief used to create checkboxes in the qcomboboxes
     */
    QStandardItemModel* Model;

    /**
     * \brief used to create checkboxes in the qcomboxes
     */
    QStandardItemModel* Model2;

    /**
     * \brief stores all the past filters
     */
    QString storedFilters [10];

    /**
     * \brief indexes the applied filters qcombobox
     */
    int appliedFiltersIndex = 0;

    /**
     * \brief indexes the filter list qcombobox
     */
    int filterListIndex = 0;

    /**
     * \brief contains the added filters
     */
    QString addedFilters [9];

    /**
     * \brief contains the filter entered by the user
     */
    QString addedFilterValues [9];

    /**
     * \brief keeps track of the number of filters added
     */
    int numFilters = 0;

    /**
     * \brief keeps track of the number of filters sent
     */
    int filterListSent = 1;

    /**
     * \brief _greenButton stored data required for green custom buttons
     */
    QString _greenButton;

    /**
     * \brief flag to check if the undo vector has been initialized
     */
    bool _flag;
};

#endif // TICKETLISTVIEW_H
