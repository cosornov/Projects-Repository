#ifndef EDITTICKETVIEW_H
#define EDITTICKETVIEW_H

#include <QDialog>
#include "Controllers/EditTicketViewController.h"
#include <vector>
#include <QString>
#include <string>
#include <QSqlQuery>

/**
 * \class EditTicketView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Edit Ticket Window.
 *
 *  Displays all the details of a ticket and allows
 *  the user to add comments or to edit the ticket based
 *  on: Category, Severity, Status, and Assigned To.
 *
 */

namespace Ui {
class EditTicketView;
}

class EditTicketView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * \brief EditTicketView constructor
     * \param parent
     */
    explicit EditTicketView(QWidget *parent = 0);

    /**
     * \brief destructor
     */
    ~EditTicketView();


    /****************************************************************************************************************
     *GETTERS
     **************************************************************************************************************/

    /**
     * \brief getController getter
     * \return reference to personal controller
     */
    EditTicketViewController* getController();

    /****************************************************************************************************************
     *OTHER METHODS
     **************************************************************************************************************/

    /**
     * \brief clear clear error fields
     */
    void clear();

    /**
     * \brief check check for empty fields
     * \return true if valid, false otherwise
     */
    bool check();

    /**
     * \brief fillForm fills falvid form input
     */
    void fillForm();

    /**
     * \brief updateComments updates comment vector
     */
    void updateComments();


    /**
     * @brief updateTicketHistory Updates the ticket history
     * @param ticketID Ticket number
     * @param agent_id Agent id
     * @param modified_at Date the ticket was changed
     * @param description Changes made to the ticket
     */
    void updateTicketHistory(QString ticketID, QString agent_id, QString modified_at, QString description);


public slots:
    /**
     * \brief saveClicked actions performed when save clicked
     */
    void saveClicked();

    /**
     * \brief saveClicked actions performed when cancel clicked
     */
    void cancelClicked();

    /**
     * \brief saveClicked actions performed when next clicked
     */
    void nextClicked();

    /**
     * \brief saveClicked actions performed when previous clicked
     */
    void prevClicked();

    /**
     * \brief saveClicked actions performed when delete clicked
     */
    void deleteClicked();

    /**
     * \brief saveClicked actions performed when add clicked
     */
    void addClicked();

    /**
     * \brief saveClicked actions performed when edit clicked
     */
    void editClicked();

    /**
     * \brief saveClicked actions performed when view notes buton clicked
     */
    void noteClicked();
    
private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::EditTicketView *ui;

    /**
     * \brief _controller reference to personal controller
     */
    EditTicketViewController *_controller;

    /**
     * \brief _ticketId stored ticket id being edited
     */
    int _ticketId;

    /**
     * \brief _changes stored changes of the ticket
     */
    std::vector<QString> _changes;

    /**
     * \brief _query stored query
     */
    QSqlQuery _query;

    /**
     * \brief _greenButton stored data for green custom buttons
     */
    QString _greenButton;
};

#endif // EDITTICKETVIEW_H
