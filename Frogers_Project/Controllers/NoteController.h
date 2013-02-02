#ifndef NOTECONTROLLER_H
#define NOTECONTROLLER_H

#include "Backend/DatabaseController.h"

/**
 * \class NoteController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the NoteView
 */
class NoteController
{
public:
    /**
     * \brief Constructor that generates a new NoteController
     */
    NoteController();

    /**
     * \brief getName gets user based on ID
     * \param id id of user to find
     * \return first name and last name of user
     */
    QString getName(int id);
    /**
     * \brief returnNoteVector
     * \return
     */
    std::vector<Comment> returnNoteVector();
    /**
     * @brief getNotes returns all notes associated with Ticket ID
     * @param id id of Ticket associated with notes
     * @return vector storing all secret notes
     */
    std::vector<Comment> getNotes(int id);
    /**
     * @brief getCurrentAgent returns Agent id associated with ticket
     * @return ID of agent assigned to current ticket
     */
    int getCurrentAgent();
    /**
     * @brief getCurrentTicket returns Ticket id of current ticket
     * @return ID of Ticket
     */
    int getCurrentTicket();
    /**
     * @brief getLocation Location of Comment Vector
     * @return index of currently displayed note
     */
    int getLocation();
    /**
     * @brief incLocation increments location of Comment vector
     */
    void incLocation();
    /**
     * @brief decLocation decrements location of Comment vector
     */
    void decLocation();
    /**
     * @brief insertNote Inserts a new note into the databse based on given parameters
     * @param ticket ID of Ticket associated with Note
     * @param agent id of Agent associated with Note
     * @param body body of the Note
     * @param time time at which Note is created
     */
    void insertNote(int ticket, int agent, QString body, QDateTime time);
    /**
     * @brief deleteNote Deletes specified note associated with current Ticket
     * @param commentID ID of note to destroy
     */
    void deleteNote(int commentID);
    /**
     * @brief editNote Changes body of specified note
     * @param commentID ID of Note to modify
     * @param body new Note body to overwrite old body
     */
    void editNote(int commentID,QString body);
    /**
     * @brief insertData stores instance of current Ticket ID and Agent ID. This allows for easy access for populating fields
     * @param ticketID Ticket ID to store
     * @param agentID Agent ID to store
     */
    void insertData(int ticketID, int agentID);
    /**
     * @brief getAgentOfTicket returns Agent associated with specified Ticket
     * @param ticketID ID of Ticket
     * @return Agent assigned to Ticket
     */
    int getAgentOfTicket(int ticketID);

private:
    DatabaseController *_db;
    std::vector<Comment> _notes;
    int _location;
    int _agentID;
    int _ticketID;


};

#endif // NOTECONTROLLER_H
