#ifndef DATABASEOBJECT_H
#define DATABASEOBJECT_H

/**
 * \class DatabaseObject
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that represents a wrapper class used for database
 * representation.
 */

#include <QString>

class DatabaseObject
{
public:
    /**
     * \brief DatabaseObject constructor
     * \param id unique id of object
     */
    DatabaseObject(int id);

    /**
     * \brief getType getter
     * \return the type of the database object
     */
    QString getType();

    /**
     * \brief getId getter
     * \return the unique id of the object
     */
    int getId();

    /**
     * \brief setId setter
     * \param id the unique id of the object
     */
    void setId(int id);

protected:
    /**
     * \brief _id stored id of the database object
     */
    int _id;

    /**
     * \brief _type stored type of the database object
     */
    QString _type;
};

#endif // DATABASEOBJECT_H
