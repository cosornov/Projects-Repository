#ifndef TICKET_H
#define TICKET_H

/**
 * \class Ticket
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that wraps data for a ticket object.
 */

#include "DatabaseObject.h"
#include <QString>
#include <QDateTime>


class Ticket : public DatabaseObject
{
public:
    /**
     * \brief Ticket constructor
     * \param id unique of of the ticket
     * \param userId user ID associated with the ticket
     * \param categoryid category of the ticket
     * \param severityid severity of the ticket
     * \param assignedtoid agent assigned to the ticket
     * \param status status of the ticket
     * \param subject subject of the ticket
     * \param description description of the ticket
     * \param createdat date the ticket was created
     * \param closedat date the ticket was closed
     */
    Ticket(int id, int userId, int categoryid, int severityid, int assignedtoid, QString status, QString subject, QString description, QDateTime  createdat, QDateTime  closedat);

    /**
     * \brief getUserId getter
     * \return the id of the ticket
     */
    int getUserId();

    /**
     * \brief getCategoryId getter
     * \return the category of the ticket
     */
    int getCategoryId();

    /**
     * \brief getSeverityId getter
     * \return the severity of the ticket
     */
    int getSeverityId();

    /**
     * \brief getassignedToId getter
     * \return the agent assigned to the ticket
     */
    int getassignedToId();

    /**
     * \brief getStatus getter
     * \return the status of the ticket
     */
    QString getStatus();

    /**
     * \brief getSubject getter
     * \return  the subject of the ticket
     */
    QString getSubject();

    /**
     * \brief getDescription getter
     * \return the description of the ticket
     */
    QString getDescription();

    /**
     * \brief getCreatedAt getter
     * \return the date the ticket was created
     */
    QDateTime  getCreatedAt();

    /**
     * \brief getClosedAt getter
     * \return the date the ticket was created
     */
    QDateTime  getClosedAt();


private:
    /**
     * \brief _userid stored id of ticket
     */
    int _userid;

    /**
     * \brief _categoryid stored category of ticket
     */
    int _categoryid;

    /**
     * \brief _severityid stored severity of ticket
     */
    int _severityid;

    /**
     * \brief _assignedtoid stored assigned agent of ticket
     */
    int _assignedtoid;

    /**
     * \brief _status stored status of ticket
     */
    QString _status;

    /**
     * \brief _subject stored subject of ticket
     */
    QString _subject;

    /**
     * \brief _description stored description of ticket
     */
    QString _description;

    /**
     * \brief _severity stored severity of ticket
     */
    QString _severity;

    /**
     * \brief _category stored category of ticket
     */
    QString _category;

    /**
     * \brief _createdat stored date of time of creating the ticket
     */
    QDateTime  _createdat;

    /**
     * \brief _closedat stored date of time of closing the ticket
     */
    QDateTime  _closedat;

};

#endif // TICKET_H
