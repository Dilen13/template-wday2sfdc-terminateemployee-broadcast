/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.workday.hr.EffectiveAndUpdatedDateTimeDataType;
import com.workday.hr.GetWorkersRequestType;
import com.workday.hr.TransactionLogCriteriaType;
import com.workday.hr.TransactionLogTypeObjectIDType;
import com.workday.hr.TransactionLogTypeObjectType;
import com.workday.hr.TransactionTypeReferencesType;
import com.workday.hr.WorkerRequestCriteriaType;

public class TerminatedEmployeeRequest {

	public static GetWorkersRequestType create(GregorianCalendar startDate) throws ParseException, DatatypeConfigurationException {

		/*
		 * Set data range for events
		 */
        EffectiveAndUpdatedDateTimeDataType dateRangeData = new EffectiveAndUpdatedDateTimeDataType();

        GregorianCalendar current = new GregorianCalendar();
        current.add(Calendar.SECOND, -1);
                                
		dateRangeData.setUpdatedFrom(getXMLGregorianCalendar(startDate));
        dateRangeData.setUpdatedThrough(getXMLGregorianCalendar(current));

        /*
		 * Set event type criteria filter
		 */

        // Hire Employee
        TransactionTypeReferencesType transactionTypeReferences = new TransactionTypeReferencesType();
        TransactionLogTypeObjectType transactionLogTypeObjectType = new TransactionLogTypeObjectType();
        TransactionLogTypeObjectIDType idType = new TransactionLogTypeObjectIDType();

        idType.setType("Business_Process_Type");
        idType.setValue("Terminate Employee");

        transactionLogTypeObjectType.getID().add(idType);
        transactionTypeReferences.getTransactionTypeReference().add(transactionLogTypeObjectType);
        TransactionLogCriteriaType transactionLogCriteria = new TransactionLogCriteriaType();
        transactionLogCriteria.setTransactionDateRangeData(dateRangeData);
        transactionLogCriteria.setTransactionTypeReferences(transactionTypeReferences);

		WorkerRequestCriteriaType workerRequestCriteria = new WorkerRequestCriteriaType();
		workerRequestCriteria.getTransactionLogCriteriaData().add(transactionLogCriteria);
        workerRequestCriteria.setExcludeInactiveWorkers(false);
        workerRequestCriteria.setExcludeEmployees(false);
        workerRequestCriteria.setExcludeContingentWorkers(true);

        GetWorkersRequestType getWorkersType = new GetWorkersRequestType();
        getWorkersType.setRequestCriteria(workerRequestCriteria);

		return getWorkersType;
	}

	private static XMLGregorianCalendar getXMLGregorianCalendar(GregorianCalendar date) throws DatatypeConfigurationException {
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
	}
}
