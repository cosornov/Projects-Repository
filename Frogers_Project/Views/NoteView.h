#ifndef NOTEVIEW_H
#define NOTEVIEW_H

/**
 * \class NoteView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Note View Window
 *
 * Allows the agent to make private notes that only other agents can see
 * and only the agent who created the note can edit.
 *
 */

#include <QDialog>
#include "Controllers/NoteController.h"

namespace Ui {
class NoteView;
}

class NoteView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * \brief NoteView
     * \param parent
     */
    explicit NoteView(QWidget *parent = 0);

    /**
     * \brief destructor
     */
    ~NoteView();

    /**
     * \brief clear error fields
     */
    void clear();

    /**
     * \brief updateNotes update notes vector
     */
    void updateNotes();

    /**
     * \brief insertData inserts data into controller
     * \param ticketID id of ticket
     * \param agentID id of agent
     */
    void insertData(int ticketID, int agentID);

    /**
     * \brief fillForm fills necessary input fields
     */
    void fillForm();

public slots:
    /**
     * \brief nextClicked actions performed when next button clicked
     */
    void nextClicked();

    /**
     * \brief prevClicked actions performed when previous button clicked
     */
    void prevClicked();

    /**
     * \brief deleteClicked actions performed when delete button clicked
     */
    void deleteClicked();

    /**
     * \brief addClicked actions performed when add button clicked
     */
    void addClicked();

    /**
     * \brief editClicked actions performed when edit button clicked
     */
    void editClicked();

    /**
     * \brief doneClicked actions performed when done button clicked
     */
    void doneClicked();
    
private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::NoteView *ui;

    /**
     * \brief _controller reference to personal controller
     */
    NoteController *_controller;

    /**
     * \brief reference to the custom css button
     */
    QString _greenButton;
};

#endif // NOTEVIEW_H
