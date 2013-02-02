#ifndef COMMENT_H
#define COMMENT_H

/**
 * \class Comment
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that wraps dada for a comment object.
 */

#include "DatabaseObject.h"
#include <QString>
#include <QDateTime>

class Comment : public DatabaseObject
{
public:
    /**
     * \brief Comment constructor
     * \param id id of the comment
     * \param userid user who made the comment
     * \param ticketid ticket the comment is found on
     * \param body body of the comment
     * \param createdat date the comment was created
     */
    Comment(int id, int userid, int ticketid, QString body, QDateTime createdat);

    /**
     * \brief getTicketID getter
     * \return the ticket id of the comment
     */
    int getTicketID();

    /**
     * \brief getuserID getter
     * \return the user who wrote the comment
     */
    int getuserID();

    /**
     * \brief getBody getter
     * \return  the body of the comment
     */
    QString getBody();

    /**
     * \brief getCreatedAt getter
     * \return the created date of the comment
     */
    QDateTime getCreatedAt();

    /**
     * \brief setTicketID setter
     * \param id the ticket id of the comment
     */
    void setTicketID(int id);

    /**
     * \brief setUserID setter
     * \param id the user who wrote the comment
     */
    void setUserID(int id);

    /**
     * \brief setBody setter
     * \param body the body of the comment
     */
    void setBody(QString body);

    /**
     * \brief setCreatedAt setter
     * \param date date the comment was written
     */
    void setCreatedAt(QDateTime date);

private:
    /**
     * \brief _ticketid stored ticket id of comment
     */
    int _ticketid;

    /**
     * \brief _userid stored user id of comment
     */
    int _userid;

    /**
     * \brief _body stored body of the comment
     */
    QString _body;

    /**
     * \brief _createdat stored created date of the comment
     */
    QDateTime _createdat;
};

#endif // COMMENT_H
