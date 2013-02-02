#include <QMessageBox>
#include "Views/TicketListV.h"
#include "ui_TicketListV.h"
#include "Views/EditTicketView.h"
#include "Views/HistoryView.h"
#include "QErrorMessage"
#include "QSqlQueryModel"
#include "Controllers/TicketListController.h"
#include "Views/HistoryView.h"
#include <QShortcut>
#include <iostream>
#include <QDebug>
#include <QSortFilterProxyModel>
#include <QTreeView>
#include <QItemSelectionModel>
#include <QModelIndexList>
#include <QAbstractItemModel>
#include <QFile>
#include <QMenu>
#include <Views/EditCustomerView.h>
#include <Views/NewTicketView.h>
#include <QStatusBar>

using std::vector;

TicketListV::TicketListV(QMainWindow *parent) :
    QMainWindow(parent),
    ui(new Ui::TicketListV)
{
    ui->setupUi(this);

    // Make the main screen fixed size
    this->setFixedSize(this->width(), this->height());

 _flag = false; // undo vector has not been initialize at this point

    //
    // CUSTOM CSS AREA
    //

    QPixmap pixmap(":/new/prefix1/refresh2.png");
    QIcon ButtonIcon(pixmap);
    ui->refreshButton->setIcon(ButtonIcon);
    ui->refreshButton->setIconSize(pixmap.rect().size());

    QFile data(":/new/prefix1/greenButton.qss");
    data.open(QFile::ReadOnly);
    QTextStream styleIn(&data);
    this->_greenButton = styleIn.readAll();
    data.close();

    //
    // Instantiate all the QComboboxes with the correct data
    //

    // Custom number combo box
    ui->ticketInput->addItem("10");
    ui->ticketInput->addItem("25");
    ui->ticketInput->addItem("50");
    ui->ticketInput->addItem("100");

    // Set to 25 as default
    ui->ticketInput->setCurrentIndex(1);

    // Filter list combo box
    ui->filterList->addItem("Select filter(s) to apply");
    ui->filterList->addItem("Ticket Number");
    ui->filterList->addItem("Customer First Name");
    ui->filterList->addItem("Customer Last Name");
    ui->filterList->addItem("Category");
    ui->filterList->addItem("Severity");
    ui->filterList->addItem("Status");
    ui->filterList->addItem("Assigned to");
    ui->filterList->addItem("Open date/time range");
    ui->filterList->addItem("Closed date/time range");

    ui->filterBox->setEnabled(false);
    ui->applyButton->setEnabled(false);
    ui->applyButton->setStyleSheet("QPushButton { background: grey; }");

    this->Model = new QStandardItemModel;
    QStandardItem* defaultItem = new QStandardItem;
    defaultItem->setText("Applied Filters");
    this->Model->insertRow(0, defaultItem);

    // Create all the items for the applied filters combo box
    for (int i = 1; i < 10; i++) {
        QStandardItem* tempItem = new QStandardItem;
        tempItem->setFlags(Qt::ItemIsUserCheckable | Qt::ItemIsEnabled);
        tempItem->setData(Qt::Unchecked, Qt::CheckStateRole);
        this->Model->insertRow(i, tempItem);
    }

   // Add the appropriate names for each section of the box
   this->Model->item(1,0)->setText("Ticket Number");
   this->Model->item(2,0)->setText("Customer First Name");
   this->Model->item(3,0)->setText("Customer Last Name");
   this->Model->item(4,0)->setText("Category");
   this->Model->item(5,0)->setText("Severity");
   this->Model->item(6,0)->setText("Status");
   this->Model->item(7,0)->setText("Assigned to");
   this->Model->item(8,0)->setText("Open date/time range");
   this->Model->item(9,0)->setText("Closed date/time range");

   ui->appliedFilters->setModel(this->Model);

   // Show/Hide specific columns combo box
   this->Model2 = new QStandardItemModel;
   QStandardItem* defaultItem2 = new QStandardItem;
   defaultItem2->setText("Select column(s) to hide/show");
   this->Model2->insertRow(0, defaultItem2);

   // Create all the items for the combo box
   for (int i = 1; i < 11; i++) {
       QStandardItem* tempItem2 = new QStandardItem;
       tempItem2->setFlags(Qt::ItemIsUserCheckable | Qt::ItemIsEnabled);
       tempItem2->setData(Qt::Unchecked, Qt::CheckStateRole);
       this->Model2->insertRow(i, tempItem2);
   }

  this->Model2->item(1,0)->setText("Ticket Number");
  this->Model2->item(2,0)->setText("Customer First Name");
  this->Model2->item(3,0)->setText("Customer Last Name");
  this->Model2->item(4,0)->setText("Category");
  this->Model2->item(5,0)->setText("Severity");
  this->Model2->item(6,0)->setText("Status");
  this->Model2->item(7,0)->setText("Agent First Name");
  this->Model2->item(8,0)->setText("Agent Last Name");
  this->Model2->item(9,0)->setText("Open date/time range");
  this->Model2->item(10,0)->setText("Closed date/time range");

  ui->showHideBox->setModel(this->Model2);

   // Create the view's own controller
   this->_controller = TicketListController();

   this->_currentIndex = 0;
   this->_offset = 25;

   // Convert the current index and offset
   QString currentIndex = QString::number(this->_currentIndex);
   QString offset =  QString::number(this->_offset);

    this->_model2 = new QSortFilterProxyModel();

   // Create the menu bar area
   QMenu *fileMenu;
   QMenu *editMenu;

   fileMenu = menuBar()->addMenu(tr("&File"));
   QAction *openAct =  new QAction(tr("&Open a ticket"), this);
   QAction *createAct = new QAction(tr("&Create a ticket"), this);
   QAction *cusAct = new QAction(tr("&Edit a Customer"), this);

   // Add some nice status tips
   createAct->setStatusTip(tr("Create a new Ticket"));
   cusAct->setStatusTip(tr("Edit the customer who opened the currently selected ticket"));
   openAct->setStatusTip(tr("Open selected ticket"));

   // Add options to the filemenu
   fileMenu->addAction(createAct);
   fileMenu->addAction(cusAct);
   fileMenu->addAction(openAct);

   // Edit menu
   editMenu = menuBar()->addMenu(tr("&Edit"));
   QAction *undoAct =  new QAction(tr("&Undo"), this);

   // Add status tip
   undoAct->setStatusTip(tr("Undo changes made to the last modified ticket"));

   // Give functionality to the buttons
   connect(undoAct, SIGNAL(triggered()), this, SLOT(undoClicked()));

   // Add options to the edit menu
   editMenu->addAction(undoAct);

   //
   // SIGNALS AND SLOTS
   //

   // give functionality to the buttons
   connect(ui->nextPageButton,SIGNAL(clicked()), this, SLOT(nextPageButtonClicked()));
   connect(ui->previousPageButton,SIGNAL(clicked()), this, SLOT(previousPageButtonClicked()));
   connect(ui->lastPageButton,SIGNAL(clicked()), this, SLOT(lastPageButtonClicked()));
   connect(ui->firstPageButton,SIGNAL(clicked()), this, SLOT(firstPageButtonClicked()));

   // give functionality to the buttons
   connect(createAct, SIGNAL(triggered()),this, SLOT(newTicketButtonClicked()));
   connect(cusAct, SIGNAL(triggered()), this, SLOT(editCus()));
   connect(openAct, SIGNAL(triggered()), this, SLOT(editTicket()));

   // Identify when the customer combo box is clicked
   connect(ui->ticketInput,SIGNAL(currentIndexChanged(int)), this, SLOT(updateData()));

   // Identify when the filter list combo box is clicked. Show the current filter in the text box.
   connect(ui->filterList,SIGNAL(currentIndexChanged(int)), this, SLOT(showFilter(int)));

   // Identify when they apply a filter
   connect(ui->applyButton,SIGNAL(clicked()), this, SLOT(applyFilter()));

   // Allow double-clicking of the customers to open up an edit window
   connect(ui->tableView, SIGNAL(doubleClicked(QModelIndex)), this, SLOT(editTicket()));

   // Check what filters have been checked and apply those to the table
   connect(this->Model, SIGNAL(dataChanged ( const QModelIndex&, const QModelIndex&)), this, SLOT(appliedFiltersChanged(const QModelIndex&, const QModelIndex&)));

   // Check what columns have hidden or shown
   connect(this->Model2, SIGNAL(dataChanged ( const QModelIndex&, const QModelIndex&)), this, SLOT(showHideBoxChanged(const QModelIndex&)));

   connect(ui->refreshButton, SIGNAL(clicked()), this, SLOT(updateData()));


   // RIGHT CLICK STUFF
   this->ui->tableView->setContextMenuPolicy(Qt::CustomContextMenu);
   connect(this->ui->tableView, SIGNAL (customContextMenuRequested(const QPoint&)), this, SLOT(rightClickMenu(const QPoint&)));

   this->ui->filterList->setCurrentIndex(7);
   this->ui->filterBox->setText(this->_controller.getAgentID());
   applyFilter();
   this->ui->filterList->setCurrentIndex(6);
   this->ui->filterBox->setText("Pending");
   applyFilter();
   this->ui->tableView->setColumnHidden(10, true);
   this->ui->filterList->setCurrentIndex(0);
   this->ui->filterBox->setText("");

   this->ui->tableView->sortByColumn(0, Qt::AscendingOrder);
   this->ui->tableView->setSortingEnabled(true);

   this->ui->dateTimeEdit1->hide();
   this->ui->dateTimeEdit2->hide();

   updateButtonData();
}


void TicketListV::rightClickMenu(const QPoint& pos){
    QPoint point = this->ui->tableView->mapToGlobal(pos);
    QMenu rightClickMenu;
    QAction* editTicketAction = rightClickMenu.addAction("Edit Ticket");
    QAction* editCustomerAction = rightClickMenu.addAction("Edit Customer");
    QAction* ticketHistoryAction = rightClickMenu.addAction("Ticket History");
    QAction* select = rightClickMenu.exec(point);
    if(select == editTicketAction){
        editTicket();
    }
    else if (select == editCustomerAction){
        editCus();
    }
    else if (select == ticketHistoryAction){
        QItemSelectionModel *selectModel = ui->tableView->selectionModel();
        QModelIndex row = selectModel->currentIndex();
        QString stringID = ui->tableView->model()->data(ui->tableView->model()->index(row.row(), 0)).toString();
        HistoryView *historyView = new HistoryView(0, stringID);
        historyView->exec();
    }
}


TicketListV::~TicketListV()
{
    delete ui;
}

void TicketListV::editTicket(){
     _flag = true; // vector has been initialize
     _ticketView = new EditTicketView();
    QItemSelectionModel *selectModel = ui->tableView->selectionModel();
    if (selectModel->hasSelection()){
        QModelIndex row = selectModel->currentIndex();
        QString stringID = ui->tableView->model()->data(ui->tableView->model()->index(row.row(), 0)).toString();
        _ticketView->getController()->setTicketInfo(stringID.toInt()); // im not getting the selected id ????/ ASK CONNOR
        _ticketView->fillForm();
//        _ticketView->show();
        _ticketView->exec();
         _flag = true; // vector has been initialize
    }
}
void TicketListV::editCus(){

    EditCustomerView *view = new EditCustomerView();
    QItemSelectionModel *selectModel = ui->tableView->selectionModel();
    if (selectModel->hasSelection()){
        QModelIndex row = selectModel->currentIndex();

        QString stringID = ui->tableView->model()->data(ui->tableView->model()->index(row.row(), 10)).toString();

        view->getController()->InjectCustomer(stringID.toInt());
        view->fillForm();
//        view->show();
        view->exec();
    }
}

void TicketListV::undoClicked(){

    bool update;
    QString closedDate = "NULL";

    if(_flag) // if vector has been initialized
    {
        vector<QString> changes = _ticketView->getController()->getChanges();
        if(changes.empty())
            QMessageBox::information(this, "Info", " There are no changes to undo");
        else
        {
            if(changes[2] == "Pending") // if the status is pending then set the NULL
                update = this->_controller.update(changes[0],changes[1],changes[2],changes[3], closedDate, changes[4]);

             else
                 update = this->_controller.update(changes[0],changes[1],changes[2],changes[3], changes[4]);

           if(update) // if the update was successful, display the appropiate message
           {
            QMessageBox::information(this, "Info", "the last change has been successfully undone");
            changes.clear();
            _ticketView->getController()->passChanges(changes);
           }
        }
    }
    else
         QMessageBox::information(this, "Info", " There are no changes to undo");

}

void TicketListV::showHideBoxChanged(const QModelIndex& a) {
    if (this->Model2->itemFromIndex(a)->checkState() == false){
        this->ui->tableView->setColumnHidden(this->Model2->itemFromIndex(a)->row() - 1, false);
    } else {
        this->ui->tableView->setColumnHidden(this->Model2->itemFromIndex(a)->row() - 1, true);
    }
}

void TicketListV::showFilter(int a){

    if (ui->filterList->currentText() == "Select filter(s) to apply") {
        ui->filterBox->setEnabled(false);
        ui->filterBox->setStyleSheet("QPushButton { background: grey; }");
        ui->applyButton->setEnabled(false);
        ui->applyButton->setStyleSheet("QPushButton { background: grey; }");
        this->ui->dateTimeEdit1->setEnabled(false);
        this->ui->dateTimeEdit2->setEnabled(false);
    } else {
        // We are dealing with a special range filter so hide the text box and show the range.
        if (this->ui->filterList->currentText() == "Open date/time range" || this->ui->filterList->currentText() == "Closed date/time range") {
            this->ui->dateTimeEdit1->setEnabled(true);
            this->ui->dateTimeEdit2->setEnabled(true);

            // Show the range
            this->ui->dateTimeEdit1->show();
            this->ui->dateTimeEdit1->setDisplayFormat("yyyy-MM-dd hh:mm:ss");
            this->ui->dateTimeEdit2->show();
            this->ui->dateTimeEdit2->setDisplayFormat("yyyy-MM-dd hh:mm:ss");
            this->ui->dateTime1Label->show();
            this->ui->dateTime2Label->show();
        } else {
            this->ui->dateTimeEdit1->hide();
            this->ui->dateTimeEdit2->hide();
            this->ui->dateTime1Label->hide();
            this->ui->dateTime2Label->hide();
            this->ui->filterBox->show();

            ui->filterBox->setEnabled(true);
            ui->filterBox->setStyleSheet(this->_greenButton);
            ui->filterBox->setText(this->storedFilters[a - 1]);
         }

         ui->applyButton->setEnabled(true);
         ui->applyButton->setStyleSheet(this->_greenButton);

        }
    }


void TicketListV::applyFilter(){

    if (this->ui->filterList->currentText() == "Open date/time range" || this->ui->filterList->currentText() == "Closed date/time range") {
        this->storedFilters[ui->filterList->currentIndex()- 1] = this->ui->dateTimeEdit1->dateTime().toString("yyyy-MM-dd hh:mm:ss") + this->ui->dateTimeEdit2->dateTime().toString("yyyy-MM-dd hh:mm:ss") ;
    } else
        // Set the filter
        this->storedFilters[ui->filterList->currentIndex()- 1] = this->ui->filterBox->toPlainText();

    // Change the state of the applied filters combo box to checked
    QStandardItem* newItem = new QStandardItem;
    newItem->setFlags(Qt::ItemIsUserCheckable | Qt::ItemIsEnabled);
    newItem->setData(Qt::Checked, Qt::CheckStateRole);
    newItem->setText(this->ui->filterList->currentText());

    filterListSent++;
    this->Model->setItem(this->ui->filterList->currentIndex(),newItem);

    ui->appliedFilters->setModel(this->Model);
}

// Re-calculate the data based on the new filters
void TicketListV::appliedFiltersChanged(const QModelIndex& a, const QModelIndex& b) {

    this->_currentIndex = 0;
    int index = this->Model->itemFromIndex(a)->row() - 1;

    if (this->Model->itemFromIndex(a)->checkState() == false){
        // The user unclicked a check box so we should remove that filter
        this->addedFilters[index] = "";
        this->addedFilterValues[index] = "";
        numFilters--;

        this->_currentIndex = 0;
    } else {
        if (filterListSent % 2 == 0) {
            this->addedFilters[index] = this->ui->filterList->currentText();

            // Add the corresponding filter's value
            this->addedFilterValues[index] = this->storedFilters[index];
            numFilters++;
            filterListSent++;
         } else {
            this->addedFilters[index] = this->Model->itemFromIndex(a)->text();
            // Add the corresponding filter's value
            this->addedFilterValues[index] = this->storedFilters[index];
            numFilters++;
         }
    }
         QString currentIndex = QString::number(this->_currentIndex);
         QString offset =  QString::number(this->_offset);

         this->_model1 = this->_controller.loadTicketsbyFilters(currentIndex, offset, addedFilters, addedFilterValues);

         // Insert the tickets into the table
         this->_model2->setSourceModel(this->_model1);
         this->ui->tableView->setModel(this->_model2);

         updateButtonData();
}


void TicketListV::setButtonModel(){

    QString currentIndex2 = QString::number(this->_currentIndex);
    QString offset2 =  QString::number(this->_offset);

    this->_model1 = this->_controller.loadTicketsbyFilters(currentIndex2, offset2, addedFilters, addedFilterValues);

    this->_model2->setSourceModel(this->_model1);

    //put the tickets in the table
    this->ui->tableView->setModel(this->_model2);


    updateButtonData();
}


void TicketListV::updateData(){

   QItemSelectionModel *selectModel = ui->tableView->selectionModel();
   QModelIndex row = selectModel->currentIndex();
   QString stringID = ui->tableView->model()->data(ui->tableView->model()->index(row.row(), 0)).toString();

   // Set the new offest by the QComboBox selection
   this->_offset = ui->ticketInput->currentText().toInt();

   QString currentIndex = QString::number(this->_currentIndex);
   QString offset =  QString::number(this->_offset);

   this->_model1 = this->_controller.loadTicketsbyFilters(currentIndex, offset, addedFilters, addedFilterValues);

   this->_model2->setSourceModel(this->_model1);

   // Put the customers in the table
   this->ui->tableView->setModel(this->_model2);
}


// Will check to see the current indexes and whether
// it is legal to execute one of the four page buttons.
void TicketListV::updateButtonData(){

    if ((this->_currentIndex - this->_offset) < 0){
        // Disable the previous button
        ui->previousPageButton->setDisabled(true);
        ui->previousPageButton->setStyleSheet("QPushButton { background: grey; }");
        ui->firstPageButton->setDisabled(true);
        ui->firstPageButton->setStyleSheet("QPushButton { background: grey; }");
    } else {
        // Disable the previous button / first page button as we're looking at the first 25 entries
        ui->previousPageButton->setDisabled(false);
        ui->previousPageButton->setStyleSheet(this->_greenButton);
        ui->firstPageButton->setDisabled(false);
        ui->firstPageButton->setStyleSheet(this->_greenButton);
    }

    if ((this->_currentIndex + this->_offset) >= this->_controller.resultCount()) {
            // Disable the next button / last page button as we're looking at the first 25 entries
            ui->nextPageButton->setDisabled(true);
            ui->nextPageButton->setStyleSheet("QPushButton { background: grey; }");
            ui->lastPageButton->setDisabled(true);
            ui->lastPageButton->setStyleSheet("QPushButton { background: grey; }");
    } else {
            ui->nextPageButton->setDisabled(false);
            ui->nextPageButton->setStyleSheet(this->_greenButton);
            ui->lastPageButton->setDisabled(false);
            ui->lastPageButton->setStyleSheet(this->_greenButton);
    }
}


void TicketListV::nextPageButtonClicked(){
    this->_currentIndex = this->_currentIndex  + this->_offset;

    setButtonModel();
}

void TicketListV::previousPageButtonClicked(){
     this->_currentIndex = this->_currentIndex - this->_offset;

    if (this->_currentIndex < 0) {
        this->_currentIndex = 0;
    }

    setButtonModel();
}

void TicketListV::lastPageButtonClicked(){

    this->_currentIndex = this->_controller.resultCount() - this->_offset;

    setButtonModel();
}

 void TicketListV::firstPageButtonClicked(){
    this->_currentIndex = 0;
    setButtonModel();
}
 void TicketListV::newTicketButtonClicked(){
     NewTicketView *newTicketView = new NewTicketView();
     newTicketView->exec();
 }




