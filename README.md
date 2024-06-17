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
    :feature:settings["settings"]
    :feature:onboarding["onboarding"]
    :feature:tasks["tasks"]
  end
  :feature:auth --> :design-system
  :feature:settings --> :core:datastore
  :app --> :core:database
  :app --> :design-system
  :app --> :core:datastore
  :app --> :feature:onboarding
  :app --> :feature:settings
  :app --> :feature:tasks
  :app --> :feature:auth
  :feature:onboarding --> :design-system
  :feature:onboarding --> :core:datastore
```