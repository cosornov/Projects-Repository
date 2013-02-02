#include "HistoryView.h"
#include "ui_HistoryView.h"
#include <QString>
#include <QStandardItem>
#include <QDebug>

using std::vector;

HistoryView::HistoryView(QWidget *parent, QString ticketID) :
    QDialog(parent),
    ui(new Ui::HistoryView)
{
    ui->setupUi(this);

    // Make the main screen fixed size
    this->setFixedSize(this->width(), this->height());

    this->_controller = new HistoryViewController();

    // Get all the previous versions of the currently selected ticket
    _model1 = this->_controller->getTicketHistory(ticketID);
    _model2 = new QSortFilterProxyModel();
    Model = new QStandardItemModel;

    rowCount = _model1 ->rowCount();
    columnCount = _model1 ->columnCount();

    checkedBoxes = 0;
    columnCount = columnCount;
    rowCount = rowCount;

    // Initialize the status vector
    for (int i = 0; i < rowCount; i++) {
        checkedStatus.push_back(0);
    }

    // Obtain all the data from the tickets
    for (int i = 0; i < rowCount; i++) {
        for (int j = 1; j < columnCount; j++) {

            QString data = _model1->data(_model1->index(i, j)).toString();


            if (j == columnCount - 1) {
                // Lastly extract the data that was changed - to display in the table view

                vector<QString> description = parseDescription(data);

                for (int k = 0; k < description.size(); k++) {
                    QStandardItem* tempItem = new QStandardItem;
                    tempItem->setText(description[k]);
                    Model->setItem(i, k + j + 1, tempItem);
                }
            }

            QStandardItem* tempItem = new QStandardItem;
            tempItem->setText(data);
            Model->setItem(i, j, tempItem);
        }
    }

    int versionCounter = 1;

    // Create all the checkboxes for the QCombobox
    for (int i = 0; i < _model1->rowCount(); i++) {
        for (int j = 0; j < 1; j++) {
            QStandardItem* tempItem = new QStandardItem;
            tempItem->setFlags(Qt::ItemIsUserCheckable | Qt::ItemIsEnabled);
            tempItem->setData(Qt::Unchecked, Qt::CheckStateRole);
            Model->setItem(i, j, tempItem);
            tempItem->setText( QString::number(versionCounter) + "." + QString::number(i));
            if (i == 9) {
                versionCounter++;
            }
        }
    }

    _model2->setSourceModel(Model);

    for (int i = 1; i < columnCount; i++) {
        _model2->setHeaderData(i, Qt::Horizontal, _model1->headerData(i, Qt::Horizontal));
    }

    _model2->setHeaderData(0, Qt::Horizontal, "Version");
    _model2->setHeaderData(4, Qt::Horizontal, "Category");
    _model2->setHeaderData(5, Qt::Horizontal, "Severity");
    _model2->setHeaderData(6, Qt::Horizontal, "Status");
    _model2->setHeaderData(7, Qt::Horizontal, "Agent ID");

    // Insert the customers into the table
    this->ui->tableView->setModel(_model2);
    this->ui->tableView->sortByColumn(0, Qt::AscendingOrder);
    this->ui->tableView->setSortingEnabled(true);
    this->ui->tableView->setEditTriggers(QAbstractItemView::NoEditTriggers);
    this->ui->tableView->setColumnHidden(3,true);

    // Check what columns have hidden or shown
    connect(this->Model, SIGNAL(dataChanged(QModelIndex,QModelIndex)), this, SLOT(updateTable(QModelIndex)));
}

vector<QString> HistoryView::parseDescription(QString description) {

    vector<QString> temp;
    QString segment = "";
    // Parses through the description and extracts the necessary data
    for (int i = 0; i < description.length(); i++){
        if (description.at(i) == '#') {
            temp.push_back(segment);
            segment = "";
        } else {
            segment +=  description.at(i);
        }
    }
    vector<QString> temp2 = this->_controller->getValueNames(temp[0], temp[1]);

    temp[0] = temp2[0];
    temp[1] = temp2[1];

    return temp;
}

// If the user selects two checkboxes then the two rows will be highlighted and their changes shown.
void HistoryView::updateTable(QModelIndex index) {

    // Check whether a checkbox has been unchecked
    if (Model->itemFromIndex(index)->checkState() == false){
        checkedStatus[Model->itemFromIndex(index)->row()] = 0;
        checkedBoxes--;
    } else {
        // Checkbox has been checked
        checkedStatus[Model->itemFromIndex(index)->row()] = 1;
        checkedBoxes++;
    }

    // Two boxes were checked so show only those two rows and highlight the changes.
    if (checkedBoxes == 2){
        hideRows();
    } else {
        showRows();
    }
}

void HistoryView::hideRows() {
    for (int i = 0; i < sizeof(checkedStatus); i++) {
        if (checkedStatus[i] == 0){
            this->ui->tableView->setRowHidden(i, true);
        }
    }
}

void HistoryView::showRows() {
    for (int i = 0; i < sizeof(checkedStatus); i++) {
        this->ui->tableView->setRowHidden(i, false);
    }
}

HistoryView::~HistoryView()
{
    delete ui;
}
