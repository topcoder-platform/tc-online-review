/**
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 *
 * This js script is to be used in edit project payments page.
 *
 * @author flexme
 * @version 1.0 (Online Review - Project Payments Integration Part 2 v1.0)
 */

/**
 * Update the disabled status for DOM elements.
 * @param eles the DOM elements
 * @param disabled the disabled status
 */
function changeDisableStatus(eles, disabled) {
    for (var i = 0; i < eles.length; i++) {
        eles[i].disabled = disabled;
        // if (disabled == "disabled") {
        //     if (!selectWrap.classList.contains("disabled")) {
        //        selectWrap.classList.add("disabled");
        //     }
        // } else {
        //     selectWrap.classList.remove("disabled");
        // }
        // disabled ? selectWrap.classList.add("disabled") : selectWrap.classList.remove("disabled");
    }
}

function changeSelectState(eles, disabled) {
    for (var i = 0; i < eles.length; i++) {
        let selectWrap = eles[i].parentNode.getElementsByClassName("select-selected")[0];
        if (disabled == "disabled") {
            if (!selectWrap.classList.contains("disabled")) {
               selectWrap.classList.add("disabled");
            }
        } else {
            selectWrap.classList.remove("disabled");
        }
    }
}
/**
 * The handler when "Automatic/Manual" radio button is clicked.
 * @param resourceId the resource id
 * @param automatic true if "Automatic" button is clicked, false if "Manual" button is clicked
 */
function changeAutomatic(resourceId, automatic) {
    document.getElementById("add_payment_" + resourceId).style.display = automatic ? "none" : "inline";
    var deleteBtns = dojo.html.getElementsByClass("delete_payment_" + resourceId);
    for (var i = 0; i < deleteBtns.length; i++) {
        var paid = deleteBtns[i].getAttribute("rel");
        if (paid != "true") {
            deleteBtns[i].style.display = automatic ? "none" : "inline";
        }
    }
    var trs = dojo.html.getElementsByClass("tr_payment_" + resourceId);
    for (var i = 0; i < trs.length; i++) {
        var paid = trs[i].getAttribute("rel");
        if (paid != "true") {
            changeDisableStatus(trs[i].getElementsByTagName("select"), automatic ? "disabled" : "");
            changeSelectState(trs[i].getElementsByTagName("select"), automatic ? "disabled" : "");
            changeDisableStatus(trs[i].getElementsByTagName("input"), automatic ? "disabled" : "");
        }
    }
}
/**
 * The handler when "Delete" payment button is clicked.
 * @param obj the "Delete" button DOM element
 * @returns {boolean} false
 */
function deletePayment(obj) {
    var resourceId = obj.getAttribute("resourceId");
    dojo.html.removeNode(obj.parentNode.parentNode);
    patchChildrenIndex(dojo.html.getElementsByClass("tr_payment_" + resourceId), obj.getAttribute("resourceIdx"));
    return false;
}
/**
 * Copy "rel" attribute to "name" for the DOM elements.
 * @param eles the DOM elements
 */
function assignRelToName(eles) {
    for (var i = 0; i < eles.length; i++) {
        eles[i].name = eles[i].getAttribute("rel");
    }
}
/**
 * Patch the name of the DOM elements with corresponding indexes. The name of
 * the DOM elements should contain two indexes like "aaa[].bbb[]".
 * @param eles the DOM elements
 * @param firstIndex the first index
 * @param secondIndex the second index
 */
function patchElementsIndex(eles, firstIndex, secondIndex) {
    for (var i = 0; i < eles.length; i++) {
        var name = eles[i].name;
        var idx = name.indexOf("[");
        var idx2 = name.indexOf("]");
        if (idx > 0 && idx2 > idx) {
            eles[i].name = name.substr(0, idx + 1) + firstIndex + name.substr(idx2);
        }
        name = eles[i].name;
        idx = name.indexOf("[", idx + 1);
        idx2 = name.indexOf("]", idx + 1);
        if (idx > 0 && idx2 > idx) {
            eles[i].name = name.substr(0, idx + 1) + secondIndex + name.substr(idx2);
        }
    }
}
/**
 * Patch the name of the DOM elements with corresponding indexes. The name of
 * the DOM elements should contain two indexes like "aaa[].bbb[]". The first index is fixed,
 * the second index will be increasing.
 * @param eles the DOM elements
 * @param firstIdx the fixed first index
 */
function patchChildrenIndex(eles, firstIdx) {
    for (var i = 0; i < eles.length; i++) {
        patchElementsIndex(eles[i].getElementsByTagName("input"), firstIdx, i);
        patchElementsIndex(eles[i].getElementsByTagName("select"), firstIdx, i);
    }
}
/**
 * The handler when "Add Payment" button is clicked.
 * @param btn the "Add Payment" button DOM element
 * @param tablePrefix the id prefix of the table which contains the button
 * @param resourceIdx the index of the corresponding resource
 * @param resourceId the id of the corresponding resource
 * @returns {boolean} false
 */
function addPayment(btn, tablePrefix, resourceIdx, resourceId) {
    var tr = btn.parentNode.parentNode;
    var table = document.getElementById(tablePrefix + "-table");
    var newTr = cloneInputRow(table.rows[2]);
    newTr.style.display = "table-row";
    newTr.className = "tr_payment_" + resourceId + " newTr";
    assignRelToName(newTr.getElementsByTagName("input"));
    assignRelToName(newTr.getElementsByTagName("select"));
    var aobj = newTr.cells[3].getElementsByTagName("a")[0];
    aobj.className = "delete_payment_" + resourceId + " deletePayment";
    aobj.setAttribute("resourceId", resourceId);
    aobj.setAttribute("resourceIdx", resourceIdx);
    if (tablePrefix == "submitters") {
        var options = [];
        if (contestSubmissions[resourceId]) options[contestPaymentTypeId] = contestPaymentTypeText;
        if (checkpointSubmissions[resourceId]) options[contestCheckpointPaymentTypeId] = checkpointPaymentTypeText;
        setSelectOptions(newTr.cells[0].getElementsByTagName("select")[0], options);
    }

    var existingTrs = dojo.html.getElementsByClass("tr_payment_" + resourceId);
    if (existingTrs.length == 0) {
        dojo.html.insertAfter(newTr, tr);
    } else {
        dojo.html.insertAfter(newTr, existingTrs[existingTrs.length - 1]);
    }
    patchChildrenIndex(dojo.html.getElementsByClass("tr_payment_" + resourceId), resourceIdx);
    if (tablePrefix == "submitters") paymentTypeChange(newTr.cells[0].getElementsByTagName("select")[0]);
    customSelect(newTr.getElementsByClassName("selectCustom-add"));
    return false;
}

function customSelect(selectWrapper) {
    for (let i = 0; i < selectWrapper.length; i++) {
        const selectElem = selectWrapper[i].getElementsByTagName("select")[0];
        const a = document.createElement("div");
        a.setAttribute("class", "select-selected");
        if (selectElem.disabled) {
            a.classList.add("disabled");
        }
        a.innerHTML = selectElem.options[selectElem.selectedIndex].innerHTML;
        selectWrapper[i].appendChild(a);

        const b = document.createElement("div");

        b.setAttribute("class", "select-items select-hide");
        for (let j = 0; j < selectElem.length; j++) {
            const c = document.createElement("div");
            c.innerHTML = selectElem.options[j].innerHTML;
            c.addEventListener("click", function(e) {
                const s = this.parentNode.parentNode.getElementsByTagName("select")[0];
                const h = this.parentNode.previousSibling;
                for (let i = 0; i < s.length; i++) {
                    if (s.options[i].innerHTML == this.innerHTML) {
                        s.selectedIndex = i;
                        h.innerHTML = this.innerHTML;
                        y = this.parentNode.getElementsByClassName("same-as-selected");
                        for (let k = 0; k < y.length; k++) {
                        y[k].removeAttribute("class");
                        }
                        this.setAttribute("class", "same-as-selected");
                        break;
                    }
                }
                h.click();
            });
            b.appendChild(c);
        }
        selectWrapper[i].appendChild(b);
        a.addEventListener("click", function(e) {
            e.stopPropagation();
            closeAllSelect(this);
            if (!selectElem.disabled) {
                this.nextSibling.classList.toggle("select-hide");
                this.classList.toggle("select-arrow-active");
            }
        });
        document.addEventListener("click", closeAllSelect);
    }
}

function closeAllSelect(elmnt) {
  const arrNo = [];
  const x = document.getElementsByClassName("select-items");
  const y = document.getElementsByClassName("select-selected");

  for (let i = 0; i < y.length; i++) {
    if (elmnt == y[i]) {
      arrNo.push(i)
    } else {
      y[i].classList.remove("select-arrow-active");
    }
  }
  for (i = 0; i < x.length; i++) {
    if (arrNo.indexOf(i)) {
      x[i].classList.add("select-hide");
    }
  }
}
/**
 * Enable all elements in a specific form.
 * @param form the specific form
 * @returns {boolean} true
 */
function enableFormElements(form) {
    for (var i = 0; i < form.elements.length; i++) {
        if (form.elements[i].disabled) {
            var hiddenInput = document.createElement("input");
            hiddenInput.type = "hidden";
            hiddenInput.name = form.elements[i].name;
            hiddenInput.value = form.elements[i].value;
            form.appendChild(hiddenInput);
        }
    }
    return true;
}
/**
 * Sets options for a "select" DOM element.
 * @param selectObj the "select" DOM element
 * @param options the options
 */
function setSelectOptions(selectObj, options) {
    selectObj.options.length = 0;
    for (var key in options) {
        var opt = document.createElement("option");
        opt.setAttribute("value", key);
        opt.appendChild(document.createTextNode(options[key]));
        selectObj.appendChild(opt);
    }
}
/**
 * The handler when the submitter payment type is changed. It will
 * re-populate the submission ids
 * @param selectObj the DOM element of the payment type widget
 */
function paymentTypeChange(selectObj) {
    var tr = selectObj.parentNode.parentNode.parentNode;
    var resourceId = tr.cells[3].getElementsByTagName("a")[0].getAttribute("resourceid");
    var options = [];
    var subs = [];
    if (selectObj.value == contestPaymentTypeId) {
        subs = contestSubmissions[resourceId];
    } else if (selectObj.value == contestCheckpointPaymentTypeId) {
        subs = checkpointSubmissions[resourceId];
    }
    for (var i = 0; i < subs.length; i++) options[subs[i]] = subs[i];
    setSelectOptions(tr.cells[1].getElementsByTagName("select")[0], options);
}
/**
 * The handler when a tab is clicked.
 * @param tableId the id of the table which should be displayed
 * @param aobj the DOM element of the link which has been clicked
 */
function showTab(tableId, aobj) {
    document.getElementById("submitters-table").style.display = "none";
    document.getElementById("reviewers-table").style.display = "none";
    document.getElementById("copilots-table").style.display = "none";
    document.getElementById(tableId).style.display = "table";
    var lis = document.getElementsByClassName("projectDetails__tab");
    for (var i = 0; i < lis.length; i++) lis[i].className = "projectDetails__tab";
    aobj.parentNode.className = "projectDetails__tab projectDetails__tab--active";
}
/**
 * The handler when the document body loaded. It will make the disable/visible state consistent with
 * the Automatic/Manual radios.
 */
function bodyLoad() {
    var trs = document.getElementsByClassName("resource-row");
    for (var i = 0; i < trs.length; i++) {
        var tr = trs[i];
        var resourceId = tr.getAttribute("resourceId");
        var radios = tr.cells[2].getElementsByTagName("input");
        var value = radios[0].checked ? radios[0].value : radios[1].value;
        changeAutomatic(resourceId, value == "true");
    }
}