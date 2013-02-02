#ifndef DASHBOARDVIEW_H
#define DASHBOARDVIEW_H

#include <QDialog>
#include "Controllers/DashboardController.h"
#include <QSortFilterProxyModel>
#include <QCloseEvent>

/**
 * \class DashboardView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the main window of the entire application.
 *
 * Contains a tab widget used to hold the other views such as:
 * the Ticket List view, Customer List view and the Edit Profile view.
 * Destroys the connection to the database upon being closed.
 *
 */

namespace Ui {
class DashboardView;
}

class DashboardView : public QDialog
{
    Q_OBJECT

public:
    /**
     * \brief dashboardView constructor
     * \param parent
     */
    explicit DashboardView(QWidget *parent = 0);

    /**
     * \brief destructor
     */
    ~DashboardView();

public slots:
    /**
     * \brief actions performed once the load button is clicked. Loads number
     * of customers specified.
     */
    void closeEvent(QCloseEvent *event);

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::DashboardView *ui;

    /**
     * \brief stored reference to database controller
     */
    DashboardController _controller;

    /**
     * \brief db Reference to the Frogers database.
     */
    QSqlDatabase db;
};

#endif // DASHBOARDVIEW_H
