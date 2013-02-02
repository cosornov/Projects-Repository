#include "DashboardView.h"
#include "ui_DashboardView.h"
#include "Views/EditCustomerView.h"
#include "Views/CustomerListView.h"
#include "Views/NewTicketView.h"
#include "Views/EditAgentView.h"
#include "Views/EditAgentView.h"
#include "Views/NewTicketView.h"
#include "Views/TicketListV.h"
#include <QErrorMessage>
#include <QSqlQueryModel>
#include <QSqlQuery>
#include <QShortcut>
#include <QDebug>
#include <QSize>

DashboardView::DashboardView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::DashboardView)
{
    QShortcut *shortcut = new QShortcut(QKeySequence("Return"),this);
    ui->setupUi(this);

    // Make the main screen fixed size
    this->setFixedSize(this->width(), this->height());

    // Create the controller and model
    this->_controller = DashboardController(); // delete methods from dashboard controller because they are in ticketListView

    // delete the first tab
    ui->tabWidget->removeTab(0);

    EditAgentView *editAgentView = new EditAgentView();
    CustomerListView *customerListView = new CustomerListView();
    TicketListV *ticketListV = new TicketListV();
    ui->tabWidget->removeTab(0); // delete the second tab

    ui->tabWidget->addTab(ticketListV, "Ticket List");
    QSize a(2,5);
    ui->tabWidget->setIconSize(a);
    ui->tabWidget->addTab(customerListView, "Customer List");
    ui->tabWidget->addTab(editAgentView, "Edit Profile");


    QPixmap pixmap(":/new/prefix1/frogersBanner.png");
    ui->frogersBanner->setPixmap(pixmap);
   // ui->frogersBanner->setIconSize(pixmap.rect().size());
}



DashboardView::~DashboardView()
{
    delete ui;
}

void DashboardView::closeEvent(QCloseEvent *event)
 {
    this->db = DatabaseController::getInstance()->getDataBase();
    this->db.close();
    qApp->quit();
 }


