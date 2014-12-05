/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jade_layout;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class contains the data needed to create a new order.
 *
 * @author Amjad
 */
public class Order {

    /**
     * (DataName)
     */
    private long id;

    /**
     * Values can be Transport or Leerfahrt.
     */
    private String auftragsart="";

    /**
     * Integer representation of priority, 0 is false, 1is true
     */
    private int hohePrioritaet;

    /**
     * Values can be Position, Modul, TE.
     */
    private String startType;
    /**
     * Values can be Position, Modul.
     */
    private String ZielTyp;

    private String startModul;
    private String zielModul;
    private String startGWP;
    private String startLWP;
    private String zielGWP;
    private String zielLWP;
    private String startXPosition;
    private String zielXPosition;
    private String startYSchatlstellung;
    private String zielYSchatlstellung;
    private String startZLWHohe;
    private String zielZLWHohe;
    private String startGreifer;
    private String zielGreifer;
    /**
     * Values can be None, Aufnahme, Abgabe
     */
    private String startLastWechsel;
    /**
     * Values can be None, Aufnahme, Abgabe
     */
    private String zielLastWechsel;
    /**
     * Values can be Manuell, Automatik
     */
    private String startZugriffsart;
    /**
     * Values can be Manuell, Automatik
     */
    private String zielZugriffsart;
    private String einlasszeit;
    private String erledigt;
    private String reserviertFuer;
 

   
  public Order() {
        // Set the defualt values for the case of Leerfahrt:
        setHohePrioritaet(0);
        setReserviertFuer("none");
        
        setStartType("Position");
        setStartLastWechsel("None");
        setStartZugriffsart("Manuell");
        setStartGreifer("B-----");
        
        setZielTyp("Position");
        setZielLastWechsel("None");
        setZielZugriffsart("Manuell");
        setZielGreifer("B-----");
        
        
        
    }
  
  
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuftragsart() {
        return auftragsart;
    }

    public void setAuftragsart(String auftragsart) {
        this.auftragsart = auftragsart;
    }

    public int getHohePrioritaet() {
        return hohePrioritaet;
    }

    public void setHohePrioritaet(int hohePrioritaet) {
        this.hohePrioritaet = hohePrioritaet;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getZielTyp() {
        return ZielTyp;
    }

    public void setZielTyp(String zielTyp) {
        ZielTyp = zielTyp;
    }

    public String getStartModul() {
        return startModul;
    }

    public void setStartModul(String startModul) {
        this.startModul = startModul;
    }

    public String getZielModul() {
        return zielModul;
    }

    public void setZielModul(String zielModul) {
        this.zielModul = zielModul;
    }

    public String getStartGWP() {
        return startGWP;
    }

    public void setStartGWP(String startGWP) {
        this.startGWP = startGWP;
    }

    public String getStartLWP() {
        return startLWP;
    }

    public void setStartLWP(String startLWP) {
        this.startLWP = startLWP;
    }

    public String getZielGWP() {
        return zielGWP;
    }

    public void setZielGWP(String zielGWP) {
        this.zielGWP = zielGWP;
    }

    public String getZielLWP() {
        return zielLWP;
    }

    public void setZielLWP(String zielLWP) {
        this.zielLWP = zielLWP;
    }

    public String getStartXPosition() {
        return startXPosition;
    }

    public void setStartXPosition(String startXPosition) {
        this.startXPosition = startXPosition;
    }

    public String getZielXPosition() {
        return zielXPosition;
    }

    public void setZielXPosition(String zielXPosition) {
        this.zielXPosition = zielXPosition;
    }

    public String getStartYSchatlstellung() {
        return startYSchatlstellung;
    }

    public void setStartYSchatlstellung(String startYSchatlstellung) {
        this.startYSchatlstellung = startYSchatlstellung;
    }

    public String getZielYSchatlstellung() {
        return zielYSchatlstellung;
    }

    public void setZielYSchatlstellung(String zielYSchatlstellung) {
        this.zielYSchatlstellung = zielYSchatlstellung;
    }

    public String getStartZLWHohe() {
        return startZLWHohe;
    }

    public void setStartZLWHohe(String startZLWHohe) {
        this.startZLWHohe = startZLWHohe;
    }

    public String getZielZLWHohe() {
        return zielZLWHohe;
    }

    public void setZielZLWHohe(String zielZLWHohe) {
        this.zielZLWHohe = zielZLWHohe;
    }

    public String getStartGreifer() {
        return startGreifer;
    }

    public void setStartGreifer(String startGreifer) {
        this.startGreifer = startGreifer;
    }

    public String getZielGreifer() {
        return zielGreifer;
    }

    public void setZielGreifer(String zielGreifer) {
        this.zielGreifer = zielGreifer;
    }

    public String getStartLastWechsel() {
        return startLastWechsel;
    }

    public void setStartLastWechsel(String startLastWechsel) {
        this.startLastWechsel = startLastWechsel;
    }

    public String getZielLastWechsel() {
        return zielLastWechsel;
    }

    public void setZielLastWechsel(String zielLastWechsel) {
        this.zielLastWechsel = zielLastWechsel;
    }

    public String getStartZugriffsart() {
        return startZugriffsart;
    }

    public void setStartZugriffsart(String startZugriffsart) {
        this.startZugriffsart = startZugriffsart;
    }

    public String getZielZugriffsart() {
        return zielZugriffsart;
    }

    public void setZielZugriffsart(String zielZugriffsart) {
        this.zielZugriffsart = zielZugriffsart;
    }

    public String getEinlasszeit() {
        return einlasszeit;
    }

    public void setEinlasszeit(String einlasszeit) {
        this.einlasszeit = einlasszeit;
    }

    public String getErledigt() {
        return erledigt;
    }

    public void setErledigt(String erledigt) {
        this.erledigt = erledigt;
    }

    public String getReserviertFuer() {
        return reserviertFuer;
    }

    public void setReserviertFuer(String reserviertFuer) {
        this.reserviertFuer = reserviertFuer;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", auftragsart=" + auftragsart + ", hohePrioritaet=" + hohePrioritaet + ", startType=" + startType + ", ZielTyp=" + ZielTyp + ", startModul=" + startModul + ", zielModul=" + zielModul + ", startGWP=" + startGWP + ", startLWP=" + startLWP + ", zielGWP=" + zielGWP + ", zielLWP=" + zielLWP + ", startXPosition=" + startXPosition + ", zielXPosition=" + zielXPosition + ", startYSchatlstellung=" + startYSchatlstellung + ", zielYSchatlstellung=" + zielYSchatlstellung + ", startZLWHohe=" + startZLWHohe + ", zielZLWHohe=" + zielZLWHohe + ", startGreifer=" + startGreifer + ", zielGreifer=" + zielGreifer + ", startLastWechsel=" + startLastWechsel + ", zielLastWechsel=" + zielLastWechsel + ", startZugriffsart=" + startZugriffsart + ", zielZugriffsart=" + zielZugriffsart + ", einlasszeit=" + einlasszeit + ", erledigt=" + erledigt + ", reserviertFuer=" + reserviertFuer + '}';
    }


}
