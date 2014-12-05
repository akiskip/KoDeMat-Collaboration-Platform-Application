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
package flw.energieeffizienz;

/**
 *
 * @author Kipouridis
 */
public interface IFacilityCommunicator {
    
    //start the service to communicate with the facility
    public void startCommunicationService();
    
    public void stopCommunicationService();
    
    public void startReceiveMessage(String msg);
    
    public void sendMessage(String msg);
    
    
}
