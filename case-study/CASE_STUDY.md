# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
Accurate allocation needs agreed cost objects (warehouse, store, lane, order and SKU), allocation drivers
(labour hours, pallet positions, picks, distance and weight), accounting periods, and treatment of fixed versus
variable cost. Inventory valuation, returns, shared labour and transfers are frequent double-counting risks.
Keep an auditable source transaction, driver version, allocation run and adjustment reason; reconcile each run
to the general ledger. I would ask which decisions consume the figures, what granularity is material, who owns
disputed allocations, and how quickly period-end close must complete.

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
Establish a cost-to-serve baseline and rank opportunities by annualized benefit, confidence, delivery cost,
customer impact and operational risk. Levers include slotting/pick-path changes, labour scheduling, shipment
consolidation, route/carrier optimization, packaging, inventory positioning, energy use and automation.
Pilot a reversible high-value change with guardrails such as fill rate, OTIF, damage and safety; scale only
after measured savings. This avoids shifting cost to another function or degrading service quality.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Integration provides a reconciled source for actuals, commitments and accruals, enabling faster close and
timely variance alerts. Use versioned idempotent events/API contracts with stable business keys, a transactional
outbox, retries/dead letters and explicit master-data ownership. Reconciliation should compare totals and
counts at each boundary and permit safe replay. Least-privilege service accounts, encryption, audit logs and
retention controls are mandatory. The design should separate real-time operational visibility from controlled
ledger posting when needed.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Budgets turn expected demand, capacity and service targets into an accountable plan; rolling forecasts make it
responsive to change. Inputs include order/SKU/channel mix, seasonality, promotions, labour rates, carrier
contracts, leases/utilities, inventory policy, capacity and network changes. Keep approved budget versions
separate from forecast and actuals, support driver-based scenarios, and show root causes for variance. Monitor
forecast bias/error at the decision level with a regular review cadence and named assumption owners.

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Replacement has transition costs (fit-out, dual running, migration, severance, transport and systems) plus
ongoing run-rate effects. Preserving immutable retired-warehouse history is essential for auditability,
period comparisons and post-investment review when a business-unit code is reused. I would use a surrogate
physical-warehouse identity, lifecycle dates and links from migration costs to old and new units. The approval
case needs a baseline, contingency, milestones, capital/operating split and benefits-realization measures so
planned transition spend is not confused with avoidable budget overrun.

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
