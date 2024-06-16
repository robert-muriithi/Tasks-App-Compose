### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:datastore["datastore"]
    :core:database["database"]
  end
  subgraph :feature
    :feature:auth["auth"]
    :feature:tasks["tasks"]
    :feature:settings["settings"]
    :feature:onboarding["onboarding"]
  end
  :feature:auth --> :design-system
  :navigation --> :feature:auth
  :navigation --> :feature:tasks
  :navigation --> :feature:settings
  :navigation --> :feature:onboarding
  :feature:settings --> :core:datastore
  :app --> :core:database
  :app --> :design-system
  :app --> :navigation
  :app --> :core:datastore
  :app --> :feature:onboarding
  :app --> :feature:settings
  :app --> :feature:tasks
  :app --> :feature:auth
  :feature:onboarding --> :design-system
  :feature:onboarding --> :core:datastore
```