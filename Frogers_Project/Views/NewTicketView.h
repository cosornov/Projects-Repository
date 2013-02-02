#ifndef NEWTICKETVIEW_H
#define NEWTICKETVIEW_H

/**
 * \class NewTicketView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the New Ticket Window.
 *
 * Allows the creation of a new ticket to be inserted into the database.
 *
 */

#include <QDialog>
#include "Controllers/NewTicketController.h"

namespace Ui {
class NewTicketView;
}

class NewTicketView : public QDialog
{
    Q_OBJECT

public:
        /**
        * Constructor for the new ticket view.
        *
        *\param parent QWidget
        */
       explicit NewTicketView(QWidget *parent = 0);

        /**
        * \brief destructor
        */
       ~NewTicketView();

        /**
         * \brief  clear all error fields
         */
        void clear();

        /**
         * \brief check for empty fields
         * \return true if valid, false otherwise
         */
        bool check();

public slots:

       /**
        * \brief actions performed once the save button is clicked.
        */
       void saveClicked();

        /**
        * \brief actions performed once the cancel button is clicked. Program exits.
        */
        void cancelClicked();


private:

    /**************************************************************************
     * Instance Variables
     **************************************************************************/

    /**
     * \brief ui
     */
    Ui::NewTicketView* ui;

    /**
     * \brief _controller reference to personal controller
     */
    NewTicketController _controller;
};

#endif // NEWTICKETVIEW_H
