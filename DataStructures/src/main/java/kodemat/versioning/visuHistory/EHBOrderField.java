/* 
 * Copyright 2014 Orthodoxos Kipouridis and Moritz Roidl.
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
package kodemat.versioning.visuHistory;

import kodemat.tele.teledata.AbstractDistributableField;

/**
 *
 * @author Kipouridis
 */
public class EHBOrderField extends AbstractDistributableField{
    private int id;
    private String name;
    private String value;
 
         
    public EHBOrderField(int id, String name) {
      super(id,name);

        }
        public EHBOrderField(int id, String name, String value) {
super(id,name,value);
        }
        
        public EHBOrderField(){
            
        }
        
 
}
