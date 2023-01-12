package lib.dehaat.ledger.presentation.mapper

import com.dehaat.androidbase.helper.isNotNull
import com.dehaat.androidbase.utils.DateFormat.dd_MMM_yyy
import com.dehaat.androidbase.utils.DateUtils
import lib.dehaat.ledger.entities.abs.ABSTransactionEntity
import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.entities.creditsummary.CreditEntity
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.creditsummary.InfoEntity
import lib.dehaat.ledger.entities.creditsummary.OverdueEntity
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductsInfoEntity
import lib.dehaat.ledger.entities.detail.creditnote.SummaryEntity
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.entities.detail.invoice.LoanEntity
import lib.dehaat.ledger.entities.detail.invoice.OverdueInfoEntity
import lib.dehaat.ledger.entities.revamp.invoice.InvoiceDataEntity
import lib.dehaat.ledger.entities.revamp.invoice.ProductEntityV2
import lib.dehaat.ledger.entities.revamp.invoice.ProductsInfoEntityV2
import lib.dehaat.ledger.entities.revamp.invoicelist.InvoiceListEntity
import lib.dehaat.ledger.entities.revamp.transaction.TransactionEntityV2
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.entities.transactionsummary.ABSEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType
import lib.dehaat.ledger.presentation.model.abs.ABSTransactionViewData
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditViewData
import lib.dehaat.ledger.presentation.model.creditsummary.InfoViewData
import lib.dehaat.ledger.presentation.model.creditsummary.OverdueViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.CreditNoteDetailViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.ProductViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.ProductsInfoViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.SummaryViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.LoanViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.OverdueInfoViewData
import lib.dehaat.ledger.presentation.model.invoicelist.InvoiceListViewData
import lib.dehaat.ledger.presentation.model.revamp.creditnote.CreditNoteDetailsViewData
import lib.dehaat.ledger.presentation.model.revamp.creditnote.CreditNoteSummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.invoice.*
import lib.dehaat.ledger.presentation.model.revamp.transactions.TransactionViewDataV2
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.presentation.model.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.util.getAmountInRupees
import javax.inject.Inject

typealias ViewDataPaymentDetailSummary = lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData
typealias EntityPaymentDetailSummary = lib.dehaat.ledger.entities.detail.payment.SummaryEntity
typealias ViewDataInvoiceDetailSummary = lib.dehaat.ledger.presentation.model.detail.invoice.SummaryViewData
typealias EntityInvoiceDetailSummary = lib.dehaat.ledger.entities.detail.invoice.SummaryEntity
typealias ViewDataInvoiceDetailProductsInfo = lib.dehaat.ledger.presentation.model.detail.invoice.ProductsInfoViewData
typealias EntityInvoiceDetailProductsInfo = lib.dehaat.ledger.entities.detail.invoice.ProductsInfoEntity
typealias ViewDataInvoiceDetailProduct = lib.dehaat.ledger.presentation.model.detail.invoice.ProductViewData
typealias EntityInvoiceDetailProduct = lib.dehaat.ledger.entities.detail.invoice.ProductEntity

class LedgerViewDataMapper @Inject constructor() {

    fun toCreditSummaryViewData(data: CreditSummaryEntity) = with(data) {
        val overdueExhausted =
            overdue.totalOverdueAmount.toDoubleOrZero() > overdue.totalOverdueLimit.toDoubleOrZero()
        CreditSummaryViewData(
            credit = toCreditSummaryCreditViewData(credit),
            overdue = toCreditSummaryOverDueViewData(overdue),
            info = toCreditSummaryInfoViewData(info),
            isOrderingBlocked = overdueExhausted,
            isCreditLimitExhausted = (credit.totalAvailableCreditLimit.toDoubleOrNull()
                ?: 0.0).isSmallerThanZero(),
            isOverdueLimitExhausted = overdueExhausted
        )
    }

    fun toTransactionSummaryViewData(
        data: TransactionSummaryEntity
    ) = with(data) {
        TransactionSummaryViewData(
            purchaseAmount = purchaseAmount,
            paymentAmount = paymentAmount,
            abs = toABSViewData(abs)
        )
    }

    private fun toABSViewData(abs: ABSEntity?) = abs?.run {
        ABSViewData(amount, lastMoveScheme, showBanner)
    }

    fun toCreditLinesViewData(data: List<CreditLineEntity>) = data.map {
        toCreditLineViewData(it)
    }

    fun toTransactionsDataEntity(data: List<TransactionEntity>) = data.map {
        toTransactionViewData(it)
    }

    fun toTransactionEntity(data: List<TransactionEntityV2>) = data.map {
        val interestStartDate = if (
            it.type == TransactionType.Invoice().type ||
            it.type == TransactionType.FinancingFee().type ||
            it.type == TransactionType.Interest().type
        ) {
            null
        } else {
            it.interestStartDate
        }
        TransactionViewDataV2(
            amount = it.amount,
            creditNoteReason = it.creditNoteReason,
            date = it.date,
            erpId = it.erpId,
            interestEndDate = it.interestEndDate,
            interestStartDate = interestStartDate,
            ledgerId = it.ledgerId,
            locusId = it.locusId,
            partnerId = it.partnerId,
            paymentMode = it.paymentMode,
            source = it.source,
            sourceNo = it.sourceNo,
            type = it.type,
            unrealizedPayment = it.unrealizedPayment
        )
    }

    fun toCreditNoteDetailDataEntity(data: CreditNoteDetailEntity) = with(data) {
        CreditNoteDetailViewData(
            summary = getCreditNoteDetailSummaryViewData(summary),
            productsInfo = getCreditNoteDetailProductInfoViewData(productsInfo),
        )
    }

    fun toCreditNoteDetailsDataEntity(data: CreditNoteDetailEntity) = with(data) {
        CreditNoteDetailsViewData(
            summary = getCreditNoteDetailsSummaryViewData(summary),
            productsInfo = getProductInfoViewData(productsInfo),
        )
    }

    private fun getCreditNoteDetailsSummaryViewData(summary: SummaryEntity) = with(summary) {
        CreditNoteSummaryViewData(
            amount = amount,
            invoiceDate = invoiceDate,
            invoiceNumber = invoiceNumber,
            reason = reason,
            timestamp = timestamp
        )
    }

    private fun getProductInfoViewData(data: ProductsInfoEntityV2) = with(data) {
        ProductsInfoViewDataV2(
            count = count,
            discount = discount,
            gst = gst,
            productList = getProductList(productList),
            itemTotal = itemTotal,
            subTotal = subTotal
        )
    }

    private fun getProductInfoViewData(data: ProductsInfoEntity) = with(data) {
        ProductsInfoViewDataV2(
            count = count,
            discount = discount,
            gst = gst,
            productList = getProductList(productList),
            itemTotal = itemTotal,
            subTotal = subTotal
        )
    }

    private fun getProductList(
        productList: List<ProductEntityV2>
    ) = productList.map {
        ProductViewDataV2(
            fname = it.fname,
            name = it.name,
            priceTotal = it.priceTotal,
            priceTotalDiscexcl = it.priceTotalDiscexcl,
            quantity = it.quantity
        )
    }

    @JvmName("product_list")
    private fun getProductList(
        productList: List<ProductEntity>?
    ) = productList?.map {
        ProductViewDataV2(
            fname = it.fname,
            name = it.name,
            priceTotal = it.priceTotal,
            priceTotalDiscexcl = it.priceTotalDiscexcl,
            quantity = it.quantity.toIntOrNull()
        )
    }

    fun toPaymentDetailSummaryViewData(data: EntityPaymentDetailSummary) = with(data) {
        ViewDataPaymentDetailSummary(
            referenceId = referenceId,
            timestamp = timestamp,
            totalAmount = totalAmount,
            mode = mode,
            principalComponent = principalComponent,
            interestComponent = interestComponent,
            overdueInterestComponent = overdueInterestComponent,
            penaltyComponent = penaltyComponent,
            advanceComponent = advanceComponent,
            paidTo = paidTo,
            belongsToGapl = belongsToGapl
        )
    }

    fun toInvoiceDetailDataViewData(data: InvoiceDetailDataEntity) = with(data) {
        InvoiceDetailDataViewData(
            summary = getInvoiceDetailSummaryViewData(summary),
            loans = loans?.map { getInvoiceDetailLoanViewData(it) },
            overdueInfo = getInvoiceDetailOverdueViewData(overdueInfo),
            productsInfo = getInvoiceDetailProductInfoViewData(productsInfo),
        )
    }

    fun toInvoiceDetailsViewData(data: InvoiceDataEntity) = with(data) {
        InvoiceDetailsViewData(
            creditNotes = creditNotes.map {
                CreditNoteViewData(
                    creditNoteAmount = it.creditNoteAmount,
                    creditNoteDate = it.creditNoteDate,
                    creditNoteType = it.creditNoteType,
                    ledgerId = it.ledgerId
                )
            },
            productsInfo = getProductInfoViewData(productsInfo),
            summary = SummaryViewDataV2(
                interestBeingCharged = summary.interestBeingCharged,
                interestDays = summary.interestDays,
                interestStartDate = summary.interestStartDate,
                invoiceAmount = summary.invoiceAmount,
                invoiceDate = summary.invoiceDate,
                invoiceId = summary.invoiceId,
                processingFee = summary.processingFee,
                totalOutstandingAmount = summary.totalOutstandingAmount,
                fullPaymentComplete = summary.interestStartDate.isNotNull() && summary.totalOutstandingAmount?.toDoubleOrNull() == 0.0,
                showInterestStartDate = false
            )
        )
    }

    private fun getInvoiceDetailProductInfoViewData(data: EntityInvoiceDetailProductsInfo) =
        with(data) {
            ViewDataInvoiceDetailProductsInfo(
                count = count,
                discount = discount,
                gst = gst,
                itemTotal = itemTotal,
                subTotal = subTotal,
                productList = productList.map {
                    getInvoiceDetailProductViewData(it)
                }
            )
        }

    private fun getInvoiceDetailProductViewData(it: EntityInvoiceDetailProduct) =
        with(it) {
            ViewDataInvoiceDetailProduct(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getInvoiceDetailLoanViewData(data: LoanEntity) = with(data) {
        LoanViewData(
            loanAccountNo = loanAccountNo,
            status = status,
            amount = amount,
            invoiceContributionInLoan = invoiceContributionInLoan,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            disbursalDate = disbursalDate,
            interestFreeEndDate = interestFreeEndDate,
            financier = financier,
            belongsToGapl = belongsToGapl
        )
    }

    private fun getInvoiceDetailSummaryViewData(data: EntityInvoiceDetailSummary) = with(data) {
        ViewDataInvoiceDetailSummary(
            amount = amount,
            number = number,
            timestamp = timestamp
        )
    }

    private fun getInvoiceDetailOverdueViewData(data: OverdueInfoEntity) = with(data) {
        OverdueInfoViewData(
            overdueDate = overdueDate
        )
    }

    private fun getCreditNoteDetailProductInfoViewData(data: ProductsInfoEntity) = with(data) {
        ProductsInfoViewData(
            count = count,
            gst = gst,
            itemTotal = itemTotal,
            subTotal = subTotal,
            productList = productList?.map {
                getCreditNoteDetailProductViewData(it)
            }
        )
    }

    private fun getCreditNoteDetailProductViewData(it: ProductEntity) =
        with(it) {
            ProductViewData(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getCreditNoteDetailSummaryViewData(data: SummaryEntity) = with(data) {
        SummaryViewData(
            amount = amount,
            invoiceNumber = invoiceNumber,
            timestamp = timestamp,
            reason = reason
        )
    }

    private fun toTransactionViewData(data: TransactionEntity) = with(data) {
        TransactionViewData(
            ledgerId = ledgerId,
            type = type,
            date = date,
            amount = amount,
            erpId = erpId,
            locusId = locusId,
            creditNoteReason = creditNoteReason,
            paymentMode = paymentMode,
            source = source,
            unrealizedPayment = unrealizedPayment
        )
    }

    private fun toCreditLineViewData(data: CreditLineEntity) = with(data) {
        CreditLineViewData(
            belongsToGapl = belongsToGapl,
            lenderViewName = lenderViewName,
            creditLimit = creditLimit,
            availableCreditLimit = availableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            advanceAmount = advanceAmount
        )
    }

    private fun toCreditSummaryInfoViewData(data: InfoEntity) = with(data) {
        InfoViewData(
            totalPurchaseAmount = totalPurchaseAmount,
            totalPaymentAmount = totalPaymentAmount,
            undeliveredInvoiceAmount = undeliveredInvoiceAmount
        )
    }

    private fun toCreditSummaryOverDueViewData(data: OverdueEntity) = with(data) {
        OverdueViewData(
            totalOverdueLimit = totalOverdueLimit,
            totalOverdueAmount = getTotalOverdueAmount(data),
            minPaymentAmount = minPaymentAmount,
            minPaymentDueDate = minPaymentDueDate
        )
    }

    private fun getTotalOverdueAmount(
        data: OverdueEntity
    ) = data.totalOverdueAmount.toDoubleOrNull()?.let {
        if (it.isGreaterThanZero()) {
            data.totalOverdueAmount.getAmountInRupees()
        } else {
            null
        }
    }

    private fun toCreditSummaryCreditViewData(data: CreditEntity) = with(data) {
        CreditViewData(
            externalFinancierSupported = externalFinancierSupported,
            totalCreditLimit = totalCreditLimit,
            totalAvailableCreditLimit = totalAvailableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount
        )
    }

    fun toInvoiceListViewData(entity: List<InvoiceListEntity>) = entity.map {
        InvoiceListViewData(
            amount = it.amount,
            date = it.date,
            interestStartDate = it.interestStartDate,
            interestFreePeriodEndDate = it.interestFreePeriodEndDate,
            ledgerId = it.ledgerId,
            locusId = it.locusId,
            outstandingAmount = it.outstandingAmount,
            partnerId = it.partnerId,
            source = it.source,
            type = it.type,
            interestDays = it.interestDays
        )
    }

    private fun Double.isGreaterThanZero() = this > 0.0

    private fun Double.isSmallerThanZero() = this < 0.0

    private fun String?.toDoubleOrZero() = this?.toDoubleOrNull() ?: 0.0

    fun toABSTransactionsViewData(entityList: List<ABSTransactionEntity>?) =
        entityList.orEmpty().map {
            toABSTransactionViewData(it)
        }

    private fun toABSTransactionViewData(entity: ABSTransactionEntity) = with(entity) {
        ABSTransactionViewData(
            amount,
            orderingDate?.let { DateUtils.getDateInFormat(dd_MMM_yyy, orderingDate.toFloat()) },
            schemeName
        )
    }
}
