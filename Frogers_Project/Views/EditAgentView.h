#ifndef EDITAGENTVIEW_H
#define EDITAGENTVIEW_H

#include <QDialog>
#include "Controllers/EditAgentController.h"

/**
 * \class EditAgentView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Edit Agent Window.
 *
 *  Displays a dialog with all the necessary fields needed to edit a agent's profile:
 *  First name, Last name, Email, Username, Password, and Password Confirmation.
 *
 */

namespace Ui {
class EditAgentView;
}

class EditAgentView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * \brief EditAgentView constructor
     * \param parent
     */
    explicit EditAgentView(QWidget *parent = 0);

    /**
     * \brief destructor
     */
    ~EditAgentView();

    /**
     * \brief refresh the editable fields
     */
    void refresh();

    /**
     * \brief clears all the error fields
     */
    void clear();

public slots:

    /**
     * Actions performed once the save button is clicked.
     */
    void confirmClicked();

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::EditAgentView *ui;

    /**
     * \brief _controller reference to personal controller
     */
    EditAgentController *_controller;
};

#endif // EDITAGENTVIEW_H
