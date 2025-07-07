# ActivityMaster Additional Components List

This document provides a comprehensive list of additional services, client components, and entity classes that are part of the ActivityMaster project. This list is intended to be used as a reference for developers working on the project.

## Services

### Core Services

1. **EnterpriseService**
   - Manages enterprises
   - Creates and configures enterprises
   - Loads updates for enterprises
   - Performs post-startup operations

2. **ActivityMasterService**
   - Loads systems for enterprises
   - Loads updates for enterprises
   - Runs scripts
   - Updates partition bases

3. **SystemsService**
   - Manages systems
   - Gets systems for enterprises
   - Gets security identity tokens

4. **ClassificationService**
   - Creates and manages classifications
   - Finds classifications by name

5. **ResourceItemService**
   - Creates and manages resource items
   - Finds resource items by UUID, type, and classification
   - Gets data for resource items

6. **ArrangementsService**
   - Creates and manages arrangements
   - Finds arrangements by type and classification

7. **SecurityTokenSystem**
   - Creates and manages security tokens
   - Authenticates users
   - Manages permissions

### Module-Specific Services

1. **GeographyService**
   - Manages geographical data
   - Finds geographical locations

2. **MailService**
   - Sends and receives emails
   - Manages mailboxes

3. **ProfileService**
   - Manages user profiles
   - Updates profile information

4. **SessionService**
   - Manages user sessions
   - Authenticates users

5. **CerialMasterService**
   - Manages serial communication
   - Connects to devices

## Client Components

### Core Client Components

1. **IManageClassifications**
   - Interface for managing classifications
   - Adds, updates, and removes classifications

2. **IManageResourceItems**
   - Interface for managing resource items
   - Adds, updates, and removes resource items

3. **IManageProductTypes**
   - Interface for managing product types
   - Adds, updates, and removes product types

4. **IManageInvolvedParties**
   - Interface for managing involved parties
   - Adds, updates, and removes involved parties

5. **IManageEvents**
   - Interface for managing events
   - Adds, updates, and removes events

6. **ISystemsService**
   - Interface for managing systems
   - Gets systems for enterprises
   - Gets security identity tokens

### Module-Specific Client Components

1. **ICerialMasterService**
   - Interface for managing serial communication
   - Connects to devices

2. **IGeographyService**
   - Interface for managing geographical data
   - Finds geographical locations

3. **IMailService**
   - Interface for sending and receiving emails
   - Manages mailboxes

4. **IProfileService**
   - Interface for managing user profiles
   - Updates profile information

5. **ISessionService**
   - Interface for managing user sessions
   - Authenticates users

## Entity Classes

### Core Entity Classes

1. **Enterprise**
   - Represents an enterprise
   - Contains enterprise information

2. **Systems**
   - Represents a system
   - Contains system information

3. **Classification**
   - Represents a classification
   - Contains classification information

4. **ResourceItem**
   - Represents a resource item
   - Contains resource item information

5. **ResourceItemData**
   - Represents resource item data
   - Contains binary data for resource items

6. **ResourceItemType**
   - Represents a resource item type
   - Contains resource item type information

7. **Arrangement**
   - Represents an arrangement
   - Contains arrangement information

8. **Event**
   - Represents an event
   - Contains event information

9. **InvolvedParty**
   - Represents an involved party
   - Contains involved party information

10. **Address**
    - Represents an address
    - Contains address information

11. **Product**
    - Represents a product
    - Contains product information

12. **SecurityToken**
    - Represents a security token
    - Contains security token information

### Module-Specific Entity Classes

1. **Geography**
   - Represents a geographical location
   - Contains geographical information

2. **Mailbox**
   - Represents a mailbox
   - Contains mailbox information

3. **Profile**
   - Represents a user profile
   - Contains profile information

4. **Session**
   - Represents a user session
   - Contains session information

5. **ComPortConnection**
   - Represents a serial port connection
   - Contains connection information

## System Updates

### Core System Updates

1. **AddressBaseSetup**
   - Sets up base address data

2. **ArrangementsBaseSetup**
   - Sets up base arrangement data

3. **ClassificationBaseSetup**
   - Sets up base classification data

4. **EventsBaseSetup**
   - Sets up base event data

5. **ProductsBaseSetup**
   - Sets up base product data

6. **ResourceItemsBaseSetup**
   - Sets up base resource item data

7. **TimeServiceSetup**
   - Sets up time service data

### Module-Specific System Updates

1. **CerialMasterInstall**
   - Installs CerialMaster module

2. **GeographyInstallCountries**
   - Installs country data

3. **GeographyInstallFeatureCodes**
   - Installs feature code data

4. **GeographyInstallGlobalLanguages**
   - Installs global language data

5. **GeographyInstallTimeZones**
   - Installs time zone data

6. **MailMasterInstall**
   - Installs MailMaster module

7. **ProfileMasterInstall**
   - Installs ProfileMaster module

8. **SessionMasterInstall**
   - Installs SessionMaster module

## Activity Master Systems

### Core Systems

1. **SystemsSystem**
   - Manages systems

2. **ClassificationSystem**
   - Manages classifications

3. **ResourceItemSystem**
   - Manages resource items

4. **ArrangementSystem**
   - Manages arrangements

5. **EventSystem**
   - Manages events

6. **InvolvedPartySystem**
   - Manages involved parties

7. **AddressSystem**
   - Manages addresses

8. **ProductSystem**
   - Manages products

9. **SecurityTokenSystem**
   - Manages security tokens

### Module-Specific Systems

1. **GeographySystem**
   - Manages geographical data

2. **MailSystem**
   - Manages mail

3. **ProfileSystem**
   - Manages profiles

4. **SessionSystem**
   - Manages sessions

5. **CerialMasterSystem**
   - Manages serial communication